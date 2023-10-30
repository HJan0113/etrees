# import the necessary packages
import threading
import zipfile

import base64
import datetime
import uuid
from threading import Thread

import pymysql
import mysql_connect
import ossTool
import res_process
import utils_self

from models.experimental import attempt_load
from utils.plots import save_one_box, Annotator
from yolov5new import Darknet

from mysql import DBQuery
from camera import LoadStreams, LoadImages
from utils.general import non_max_suppression, scale_coords, check_imshow, set_logging, check_img_size, xyxy2xywh
from utils.dataloaders import letterbox
from flask import Response, make_response
from flask import Flask
from flask import render_template
from flask import request
import time
import torch
import json
import cv2
import os
import shutil
import numpy as np
from pathlib import Path

from utils.torch_utils import select_device

# initialize a flask object
app = Flask(__name__)

# initialize the video stream and allow the camera sensor to warmup
with open('yolov5_config.json', 'r', encoding='utf8') as fp:
    opt = json.load(fp)
    print('[INFO] YOLOv5 Config:', opt)

darknet = Darknet(opt)  # 卢振晟注释掉了 调摄像头 如果失败会报错 导致程序停止运行
if darknet.webcam:
    # cudnn.benchmark = True  # set True to speed up constant image size inference
    dataset = LoadStreams(darknet.source, img_size=opt["imgsz"], stride=darknet.stride)
time.sleep(2.0)


@app.route("/camera")
def index():
    # return the rendered template
    return render_template("index.html")


def detect_gen(dataset, feed_type):
    view_img = check_imshow()
    t0 = time.time()
    for path, img, img0s, vid_cap in dataset:
        img = darknet.preprocess(img)

        t1 = time.time()
        pred = darknet.model(img, augment=darknet.opt["augment"])[0]  # 0.22s
        pred = pred.float()
        pred = non_max_suppression(pred, darknet.opt["conf_thres"], darknet.opt["iou_thres"])
        t2 = time.time()

        pred_boxes = []
        for i, det in enumerate(pred):
            if darknet.webcam:  # batch_size >= 1
                feed_type_curr, p, s, im0, frame = "Camera_%s" % str(i), path[i], '%g: ' % i, img0s[
                    i].copy(), dataset.count
            else:
                feed_type_curr, p, s, im0, frame = "Camera", path, '', img0s, getattr(dataset, 'frame', 0)

            s += '%gx%g ' % img.shape[2:]  # print string
            gn = torch.tensor(im0.shape)[[1, 0, 1, 0]]  # normalization gain whwh
            if det is not None and len(det):
                det[:, :4] = scale_coords(
                    img.shape[2:], det[:, :4], im0.shape).round()

                # Print results
                for c in det[:, -1].unique():
                    n = (det[:, -1] == c).sum()  # detections per class
                    s += f"{n} {darknet.names[int(c)]}{'s' * (n > 1)}, "  # add to string

                for *xyxy, conf, cls_id in det:
                    lbl = darknet.names[int(cls_id)]
                    xyxy = torch.tensor(xyxy).view(1, 4).view(-1).tolist()
                    score = round(conf.tolist(), 3)
                    label = "{}: {}".format(lbl, score)
                    x1, y1, x2, y2 = int(xyxy[0]), int(xyxy[1]), int(xyxy[2]), int(xyxy[3])
                    pred_boxes.append((x1, y1, x2, y2, lbl, score))
                    if view_img:
                        darknet.plot_one_box(xyxy, im0, color=(255, 0, 0), label=label)

            # Print time (inference + NMS)
            # print(pred_boxes)
            print(f'{s}Done. ({t2 - t1:.3f}s)')
            if feed_type_curr == feed_type:
                frame = cv2.imencode('.jpg', im0)[1].tobytes()
                yield (b'--frame\r\n' b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n')


import numpy as np


# source is upload_path in /images
def detect(source, save_img=True):
    print('-----------------this is detect `s content-------------------')
    # 各种传参数
    # print(source)
    imgname = source.split('\\')[-1].strip()
    # print(imgname)
    out, weights, view_img, save_txt, imgsz = \
        opt.output, opt.weights, opt.view_img, opt.save_txt, opt.img_size

    # Initialize
    set_logging()
    device = select_device(opt.device)
    # print(out)
    if os.path.exists(out):
        shutil.rmtree(out)  # delete output folder
    os.makedirs(out)  # make new output folder
    # half = device.type != 'cpu'  # half precision only supported on CUDA

    # Load model
    model = attempt_load(weights, device=device)  # load FP32 model
    imgsz = check_img_size(imgsz, s=model.stride.max())  # check img_size
    half = False
    if half:
        model.half()  # to FP16

    # Set Dataloader
    vid_path, vid_writer = None, None
    # if save_img = True：
    dataset = LoadImages(source, img_size=imgsz)

    # Get names and colors
    names = model.module.names if hasattr(model, 'module') else model.names
    colors = [[np.random.randint(0, 255) for _ in range(3)] for _ in range(len(names))]

    # Run inference
    t0 = time.time()
    img = torch.zeros((1, 3, imgsz, imgsz), device=device)  # init img
    _ = model(img.half() if half else img) if device.type != 'cpu' else None  # run once
    for path, img, im0s, vid_cap in dataset:
        img = torch.from_numpy(img).to(device)
        img = img.half() if half else img.float()  # uint8 to fp16/32
        img /= 255.0  # 0 - 255 to 0.0 - 1.0
        if img.ndimension() == 3:
            img = img.unsqueeze(0)

        # Inference
        t1 = time.time()
        pred = model(img, augment=opt.augment)[0]

        # Apply NMS
        pred = non_max_suppression(pred, opt.conf_thres, opt.iou_thres, classes=opt.classes, agnostic=opt.agnostic_nms)
        num = len(pred[0].data)
        # num1 = pred.numpy()
        # print(num)
        t2 = time.time()

        # Apply Classifier
        # if classify:
        #     pred = apply_classifier(pred, modelc, img, im0s)

        # Process detections
        # print(pred)
        for i, det in enumerate(pred):  # detections per image
            # print(i,det)
            # if webcam:  # batch_size >= 1
            #    p, s, im0 = path[i], '%g: ' % i, im0s[i].copy()
            if 1:
                p, s, im0 = path, '', im0s

            save_path = str(Path(out) / Path(p).name)
            txt_path = str(Path(out) / Path(p).stem) + ('_%g' % dataset.frame if dataset.mode == 'video' else '')
            s += '%gx%g ' % img.shape[2:]  # print string
            gn = torch.tensor(im0.shape)[[1, 0, 1, 0]]  # normalization gain whwh
            annotator = Annotator(im0, line_width=3, example=str(names))
            labels = ""
            # print('@@@')
            print(det)  # 这一块 tensor对象中 第一个参数如果为空 即传过来的图片 不包含任何模型中的数据 报错 500
            # print('@@@')
            if det is not None and len(det):

                # Rescale boxes from img_size to im0 size
                det[:, :4] = scale_coords(img.shape[2:], det[:, :4], im0.shape).round()

                # Print results
                for c in det[:, -1].unique():
                    n = (det[:, -1] == c).sum()  # detections per class
                    s += '%g %ss, ' % (n, names[int(c)])  # add to string

                # Write results
                confidence_list = []
                confidence = ""
                for *xyxy, conf, cls in reversed(det):
                    # print('-------------------------------')
                    # print(xyxy)
                    if save_txt:  # Write to file
                        xywh = (xyxy2xywh(torch.tensor(xyxy).view(1, 4)) / gn).view(-1).tolist()  # normalized xywh

                        with open(txt_path + '.txt', 'a') as f:
                            f.write(('%g ' * 5 + '\n') % (cls, *xywh))  # labels format

                    if save_img or view_img:  # Add bbox to image
                        c = int(cls)
                        label = '%s %.2f' % (names[int(cls)], conf)
                        # print("label",label) # tpye + confidence
                        confidence = confidence + str(label.split(" ")[1])
                        confidence_list.append(confidence)  #
                        print("置信度：", label.split(" ")[1])
                        # print("line=196",labels.split(' ')[0])
                        labels = labels + str(label.split(' ')[0]) + "、"
                        print("labels:", labels)
                        # labels = labels
                        annotator.box_label(xyxy, label=label, color=colors[c])
                        im0 = annotator.result()
                cv2.imwrite("static/CL_" + imgname, im0)
                encoded = base64.b64encode(open("static/CL_" + imgname, 'rb').read())
                img_bin = encoded.decode()
            else:
                return 0, 0, 0, 0, 0, imgname
            print('%sDone. (%.3fs)' % (s, t2 - t1))
    print('----------detect `s content that is out --------------')
    return "CL_" + imgname, labels, num, t2 - t1, img_bin, imgname


def detect2(source, save_img=True):# mul_images
    print('-----------------this is detect `s content-------------------')
    # 各种传参数
    # print(source)
    imgname = source.split('/')[-1].strip()
    # print(imgname)
    out, weights, view_img, save_txt, imgsz = \
        opt.output, opt.weights, opt.view_img, opt.save_txt, opt.img_size

    # Initialize
    set_logging()
    device = select_device(opt.device)
    # print(out)
    if os.path.exists(out):
        shutil.rmtree(out)  # delete output folder
    os.makedirs(out)  # make new output folder
    # half = device.type != 'cpu'  # half precision only supported on CUDA

    # Load model
    model = attempt_load(weights, device=device)  # load FP32 model
    imgsz = check_img_size(imgsz, s=model.stride.max())  # check img_size
    half = False
    if half:
        model.half()  # to FP16

    # Set Dataloader
    vid_path, vid_writer = None, None
    # if save_img = True：
    dataset = LoadImages(source, img_size=imgsz)

    # Get names and colors
    names = model.module.names if hasattr(model, 'module') else model.names
    colors = [[np.random.randint(0, 255) for _ in range(3)] for _ in range(len(names))]

    # Run inference
    t0 = time.time()
    img = torch.zeros((1, 3, imgsz, imgsz), device=device)  # init img
    _ = model(img.half() if half else img) if device.type != 'cpu' else None  # run once
    for path, img, im0s, vid_cap in dataset:
        img = torch.from_numpy(img).to(device)
        img = img.half() if half else img.float()  # uint8 to fp16/32
        img /= 255.0  # 0 - 255 to 0.0 - 1.0
        if img.ndimension() == 3:
            img = img.unsqueeze(0)

        # Inference
        t1 = time.time()
        pred = model(img, augment=opt.augment)[0]

        # Apply NMS
        pred = non_max_suppression(pred, opt.conf_thres, opt.iou_thres, classes=opt.classes, agnostic=opt.agnostic_nms)
        num = len(pred[0].data)
        # num1 = pred.numpy()
        # print(num)
        t2 = time.time()

        # Apply Classifier
        # if classify:
        #     pred = apply_classifier(pred, modelc, img, im0s)

        # Process detections
        # print(pred)
        for i, det in enumerate(pred):  # detections per image
            # print(i,det)
            # if webcam:  # batch_size >= 1
            #    p, s, im0 = path[i], '%g: ' % i, im0s[i].copy()
            if 1:
                p, s, im0 = path, '', im0s

            save_path = str(Path(out) / Path(p).name)
            txt_path = str(Path(out) / Path(p).stem) + ('_%g' % dataset.frame if dataset.mode == 'video' else '')
            s += '%gx%g ' % img.shape[2:]  # print string
            gn = torch.tensor(im0.shape)[[1, 0, 1, 0]]  # normalization gain whwh
            annotator = Annotator(im0, line_width=3, example=str(names))
            labels = ""
            # print('@@@')
            print(det)  # 这一块 tensor对象中 第一个参数如果为空 即传过来的图片 不包含任何模型中的数据 报错 500
            # print('@@@')
            if det is not None and len(det):

                # Rescale boxes from img_size to im0 size
                det[:, :4] = scale_coords(img.shape[2:], det[:, :4], im0.shape).round()

                # Print results
                for c in det[:, -1].unique():
                    n = (det[:, -1] == c).sum()  # detections per class
                    s += '%g %ss, ' % (n, names[int(c)])  # add to string

                # Write results
                confidence_list = []
                confidence = ""
                for *xyxy, conf, cls in reversed(det):
                    # print('-------------------------------')
                    # print(xyxy)
                    if save_txt:  # Write to file
                        xywh = (xyxy2xywh(torch.tensor(xyxy).view(1, 4)) / gn).view(-1).tolist()  # normalized xywh

                        with open(txt_path + '.txt', 'a') as f:
                            f.write(('%g ' * 5 + '\n') % (cls, *xywh))  # labels format

                    if save_img or view_img:  # Add bbox to image
                        c = int(cls)
                        label = '%s %.2f' % (names[int(cls)], conf)
                        # print("label",label) # tpye + confidence
                        confidence = confidence + str(label.split(" ")[1])
                        confidence_list.append(confidence)  #
                        print("置信度：", label.split(" ")[1])
                        # print("line=196",labels.split(' ')[0])
                        labels = labels + str(label.split(' ')[0]) + "、"
                        print("labels:", labels)
                        # labels = labels
                        annotator.box_label(xyxy, label=label, color=colors[c])
                        im0 = annotator.result()
                cv2.imwrite("static/CL_" + imgname, im0)
                # encoded = base64.b64encode(open("static/CL_" + imgname, 'rb').read())
                # img_bin = encoded.decode()
                # print("imgname",imgname)
            else:
                return 0, 0, 0, 0, 0, imgname
            print('%sDone. (%.3fs)' % (s, t2 - t1))
    print('----------detect `s content that is out --------------')
    return "CL_" + imgname, labels, num, t2 - t1, imgname


def detect3(source, save_img=True):  # 识别模型 仅返回类别及文件名
    print('-----------------this is detect `s content-------------------')
    # 各种传参数
    # print(source)
    imgname = source.split('/')[-1].strip()
    # print(imgname)
    out, weights, view_img, save_txt, imgsz = \
        opt.output, opt.weights, opt.view_img, opt.save_txt, opt.img_size

    # Initialize
    set_logging()
    device = select_device(opt.device)
    # print(out)
    if os.path.exists(out):
        shutil.rmtree(out)  # delete output folder
    os.makedirs(out)  # make new output folder
    # half = device.type != 'cpu'  # half precision only supported on CUDA

    # Load model
    model = attempt_load(weights, device=device)  # load FP32 model
    imgsz = check_img_size(imgsz, s=model.stride.max())  # check img_size
    half = False
    if half:
        model.half()  # to FP16

    # Set Dataloader
    vid_path, vid_writer = None, None
    # if save_img = True：
    dataset = LoadImages(source, img_size=imgsz)

    # Get names and colors
    names = model.module.names if hasattr(model, 'module') else model.names
    colors = [[np.random.randint(0, 255) for _ in range(3)] for _ in range(len(names))]

    # Run inference
    t0 = time.time()
    img = torch.zeros((1, 3, imgsz, imgsz), device=device)  # init img
    _ = model(img.half() if half else img) if device.type != 'cpu' else None  # run once
    xml_list = []
    for path, img, im0s, vid_cap in dataset:
        img = torch.from_numpy(img).to(device)
        img = img.half() if half else img.float()  # uint8 to fp16/32
        img /= 255.0  # 0 - 255 to 0.0 - 1.0
        if img.ndimension() == 3:
            img = img.unsqueeze(0)

        # Inference
        t1 = time.time()
        pred = model(img, augment=opt.augment)[0]

        # Apply NMS
        pred = non_max_suppression(pred, opt.conf_thres, opt.iou_thres, classes=opt.classes, agnostic=opt.agnostic_nms)
        num = len(pred[0].data)
        t2 = time.time()

        # Apply Classifier
        # if classify:
        #     pred = apply_classifier(pred, modelc, img, im0s)

        # Process detections
        # print(pred)

        for i, det in enumerate(pred):  # detections per image
            # print(i,det)
            # if webcam:  # batch_size >= 1
            #    p, s, im0 = path[i], '%g: ' % i, im0s[i].copy()
            if 1:
                p, s, im0 = path, '', im0s

            save_path = str(Path(out) / Path(p).name)
            txt_path = str(Path(out) / Path(p).stem) + ('_%g' % dataset.frame if dataset.mode == 'video' else '')
            s += '%gx%g ' % img.shape[2:]  # print string
            gn = torch.tensor(im0.shape)[[1, 0, 1, 0]]  # normalization gain whwh
            annotator = Annotator(im0, line_width=3, example=str(names))
            labels = ""
            # print('@@@')
            print(det)  # 这一块 tensor对象中 第一个参数如果为空 即传过来的图片 不包含任何模型中的数据 报错 500

            # print('@@@')
            if det is not None and len(det):

                # Rescale boxes from img_size to im0 size
                det[:, :4] = scale_coords(img.shape[2:], det[:, :4], im0.shape).round()

                # Print results
                for c in det[:, -1].unique():
                    n = (det[:, -1] == c).sum()  # detections per class
                    s += '%g %ss, ' % (n, names[int(c)])  # add to string

                # Write results
                confidence_list = []
                confidence = ""
                type_num = 0
                for *xyxy, conf, cls in reversed(det):
                    if save_txt:  # Write to file
                        xywh = (xyxy2xywh(torch.tensor(xyxy).view(1, 4)) / gn).view(-1).tolist()  # normalized xywh
                        print(xywh)
                        with open(txt_path + '.txt', 'a') as f:
                            f.write(('%g ' * 5 + '\n') % (cls, *xywh))  # labels format

                    if save_img or view_img:  # Add bbox to image
                        c = int(cls)
                        label = '%s %.2f' % (names[int(cls)], conf)
                        # print("label",label) # tpye + confidence
                        confidence = confidence + str(label.split(" ")[1])
                        confidence_list.append(confidence)  #
                        print("置信度：", label.split(" ")[1])
                        # write xml
                        det1 = det.numpy()
                        a1 = det1[0][:4]
                        # print(type(a1))
                        b1 = []
                        for i in a1:
                            b1.append(i)

                        xml_list.append(b1)
                        # print(b1, type(b1))
                        write_type = label.split(" ")[0]
                        xml_list[type_num].append(write_type)
                        type_num += 1
                        # write xml
                        # print("line=196",labels.split(' ')[0])
                        labels = labels + str(label.split(' ')[0]) + "、"
                        print("labels:", labels)
                        # labels = labels
                        annotator.box_label(xyxy, label=label, color=colors[c])
                        im0 = annotator.result()

            else:
                return 0, 0, 0, 0, 0, imgname
            print('%sDone. (%.3fs)' % (s, t2 - t1))
    print('----------detect `s content that is out --------------')
    # print(xml_list)
    return xml_list, imgname


@app.route('/video_feed/<feed_type>')
def video_feed(feed_type):
    """Video streaming route. Put this in the src attribute of an img tag."""
    if feed_type == 'Camera_0':
        return Response(detect_gen(dataset=dataset, feed_type=feed_type),
                        mimetype='multipart/x-mixed-replace; boundary=frame')
    # 双摄像头情况下使用
    # elif feed_type == 'Camera_1':
    #     return Response(detect_gen(dataset=dataset, feed_type=feed_type),
    #                     mimetype='multipart/x-mixed-replace; boundary=frame')


@app.route('/imagesUp', methods=['POST', 'GET'])
def imagesUp():
    if request.method == 'POST':
        f1 = request.files['file']
        uuid = request.form['uuid']
        basepath = os.path.dirname(__file__)
        fileName = str(uuid) + "." + (str(f1.filename).split("."))[1]
        upload_path = os.path.join(basepath, 'static\images', fileName)
        f1.save(upload_path)
        isOssPath = "Y:\{}yolov5-base_2\{}yolov5-base\{}static\{}images\{}{}".format("\\", "\\", "\\", "\\", "\\",
                                                                            fileName)
        # base 64
        with open(isOssPath, "rb") as image_file:
            img1 = base64.b64encode(image_file.read()).decode()
        print(fileName)
        res = {'img1': img1,
               'fileName':fileName}
        return res
    return "hh"


@app.route('/images', methods=['POST', 'GET'])
def upload_image():
    # dataset = LoadImages(darknet.source, img_size=opt["imgsz"], stride=darknet.stride)
    if request.method == 'GET':
        # # 获取用户上传的图片
        # f1 = request.files['file']  # 发送参数的名字 要和这边接收参数的名字
        # # user_id = request.form['user_id']  # 获取user_id
        # # device = request.form['device']  # 获取device
        # # print("userParam",user_id, device)
        #
        # # 获取当前文件所在路径
        # basepath = os.path.dirname(__file__)
        # # print("basepath:",basepath)
        #
        # # 找到存储图片的位置
        # upload_path = os.path.join(basepath, 'static\images', f1.filename)  # 注意：没有的文件夹⼀定要先创建，不然会提示没有该路径
        # # print("upload_path:",upload_path)
        # # 在位置中存储用户上传的图片
        # f1.save(upload_path)

        fileName = request.args.get('fileName')
        basepath = os.path.dirname(__file__)
        upload_path = os.path.join(basepath, 'static\images', fileName)

        # t1 = time.time()
        # yoloV5模型加载,进行识别
        yolo_result, type, num, t, img_bin, imgname = detect(upload_path)

        # t2 = time.time()
        # yolo_result带回归框文件的名字
        # type 图片中识别出来的类别
        # num 数量 类别/回归框
        # t time
        # img_bin ？
        # imgname is img `s name

        if yolo_result == 0 and type == 0 and num == 0 and t == 0 and img_bin == 0:  # 图片未包含模型中信息

            # isOssPath = "Y:\{}yolov5-base_2\{}yolov5-base\{}static\{}images\{}{}".format("\\", "\\", "\\", "\\", "\\",
            #                                                                              imgname)
            # # base 64
            # with open(isOssPath, "rb") as image_file:
            #     encoded_string = base64.b64encode(image_file.read()).decode()

            res_1 = []
            res_1.append({
                "res1": "hjw5201314lyt",  # 请回传
                "res2": "hjw5201314lyt",
                "res3": "hjw5201314lyt"
                # "img1": encoded_string,  # 请回传
                # "img2": encoded_string
            })

            return res_1

        type = res_process.tranChin(type)  # type tran Chin
        t3 = time.time()

        # isOssPath = "Y:\{}yolov5-base_2\{}yolov5-base\{}static\{}images\{}{}".format("\\", "\\", "\\", "\\", "\\",
        #                                                                              imgname)
        # isOssPath_2 = "Y:\{}yolov5-base_2\{}yolov5-base\{}static\{}{}".format("\\", "\\", "\\", "\\", "CL_" + imgname)
        # # print(isOssPath, isOssPath_2)
        # # or_image_url, pro_image_url = ossTool.upload(isOssPath, isOssPath_2)  # up oss
        #
        #
        # # base 64
        # with open(isOssPath, "rb") as image_file:
        #     encoded_string = base64.b64encode(image_file.read()).decode()
        #
        # with open(isOssPath_2, "rb") as image_file:
        #     encoded_string_2 = base64.b64encode(image_file.read()).decode()

        # print(encoded_string)
        # print(encoded_string_2)

        res_1 = []
        res_1.append({
            "res1": type,  # 请回传
            "res2": len(type.split("、")),
            "res3": t
            # "img1": encoded_string,  # 请回传
            # "img2": encoded_string_2
        })
        result = json.dumps(res_1, ensure_ascii=False)

        t4 = time.time()
        # print(t2 - t1)
        # print(t3 - t2)
        # print(t4 - t3)
        # print(t4 - t1)


        return result
        # type=type[:-4]
        # Query = DBQuery()
        # current_time= str(datetime.datetime.now())
        # try:
        #    Query.db_execute(str(type), str(num), str(t), current_time,str(img_bin))
        # except Exception:
        #    print("base64太大，数据库存储出错")
        # return render_template('yoloresult.html', result=yolo_result,filename=f1.filename,t=t,num=num,type=type)
    return render_template('yolostart.html')

@app.route('/CL_images', methods=['POST', 'GET'])
def CL_images():
    if request.method == 'POST':
        fileName = request.form['fileName']
        basepath = os.path.dirname(__file__)
        upload_path = os.path.join(basepath, 'static', "CL_" + fileName)
        print(upload_path)
        with open(upload_path, "rb") as image_file:
            img2 = base64.b64encode(image_file.read()).decode()
        res = {'img2':img2}
        return res



# @app.route('/images_other', methods=['POST', 'GET'])
# def images_other():
#     if request.method == 'POST':
#         # print(request)
#         id = request.form['id']
#         device = request.form['device']
#         img1 = request.form['img1']
#         res1 = request.form['res1']
#
#         # print(id, device, img1, res1)
#         print(type(id), type(device), img1, res1)
#
#         imgName = str(uuid.uuid4())
#         imgUrl = "Y:\{}yolov5-base_2\{}yolov5-base\{}static\{}images{}".format("\\", "\\", "\\", "\\", "\\")
#
#         with open(imgUrl + imgName, "wb") as image_file:
#             decoded_string = base64.b64decode(img1)
#             image_file.write(decoded_string)
#
#         img1, img2 = ossTool.upload(imgUrl + imgName, imgUrl + imgName)
#
#         t1 = time.time()
#
#         mysql_connect.write_mysql(id, device, img1, res1)  # write in table name identify
#         t2 = time.time()
#
#         list2 = res_process.process(res1)  # 结果处理
#         t3 = time.time()
#
#         # req_data = json.loads(str)
#         # list = []
#         # list.append({
#         #     "q": 1,
#         #     "w": 2,
#         #     "e": 3
#         # })
#         # result = json.dumps(list, ensure_ascii=False)
#
#         print(t2 - t1)
#         print(t3 - t2)
#         print(t3 - t1)
#         return list2
#
#     return "error method not post"


# @app.route('/mul_images', methods=['POST', 'GET'])
# def mul_images():
#     # 获取用户上传的图片
#     # f1 = request.files['file']  # 发送参数的名字 要和这边接收参数的名字一致
#     # fileName = request.form['fileName']  # 获取
#     # actId = request.form['actId']  # 获取
#     # num = request.form['num']  # 获取
#
#
#
#
#     onlyName = "0613"
#     file_dir = "Y:\\yolov5-base_2\\yolov5-base\\mul_images\\" + onlyName + "\\"
#
#     list_directory = os.listdir(file_dir) #获取路径下所有文件 文件名 list类型
#
#     #创建一个写入结果的txt
#     fileName = str(uuid.uuid4()) + ".txt"
#     f = open(file_dir + fileName, 'a+')
#
#     # filelists = []
#     # for directory in list_directory:
#     #     filelists.append(directory)
#
#
#     for i in range(len(list_directory)):
#         imgpath = file_dir + str(list_directory[i]) #拼接路径
#         yolo_result, type, num, t, img_bin, imgname2 = detect(imgpath) #模型调用
#         # typeList.append(str(list_directory[i]) + ":")
#         # typeList.append(type)
#         fileNameWri = (str(list_directory[i]).split("."))[0]
#         resStr = fileNameWri + ":" + type #文件名：种类
#         f.write(resStr + "\n")
#
#     # typeList = json.dumps(typeList, ensure_ascii=False)
#
#     return "that is out"

from threading import BoundedSemaphore, Thread


@app.route('/mul_images_test', methods=['POST', 'GET'])
def mul_images_test():
    # 文件夹名 将用户所上传的压缩包 解压至此
    fileListName = str(uuid.uuid4())
    os.makedirs(r'Y:/yolov5-base_2/yolov5-base/mul_images/{}'.format(fileListName))  # 创建文件夹


    # 获取当前文件所在路径
    basepath = os.path.dirname(__file__)
    basepath = basepath.replace("\\", '/')
    # print((basepath))
    # 接收压缩包
    f1 = request.files['file']  # 发送参数的名字 要和这边接收参数的名字
    # 找到存储文件的位置
    upload_path = (basepath + '/mul_images/{}/'.format(fileListName) +f1.filename )  # 注意：没有的文件夹⼀定要先创建，不然会提示没有该路径
    # print(upload_path)
    # 在位置中存储文件
    f1.save(upload_path)

    zip_file = zipfile.ZipFile(upload_path)  # 传入压缩文件 fileListName.zip 获取相关信息 并压缩
    zip_list = zip_file.namelist()
    # print(zip_list)
    folder_abs = "Y:/yolov5-base_2/yolov5-base/mul_images/{}/".format(fileListName)
    for f in zip_list:
        zip_file.extract(f, folder_abs)  # 循环解压文件到指定目录


    imageData = []  # 结果集列表
    max_xiancshu = 1  # 最大线程数
    pool_sema = BoundedSemaphore(max_xiancshu)  # 创建线程锁 确保线程数不会超过设置最大线程数

    # file_dir = "Y:/yolov5-base_2/yolov5-base/mul_images/" + fileListName + "/" + (f1.filename.split("."))[0] + "/"

    # 压缩包解压后所在的路径
    # file_dir = "Y:\\{}yolov5-base_2\\{}yolov5-base\\{}mul_images\\{}{}\\{}{}\\".format("\\", "\\", "\\", "\\", fileListName, "\\", (f1.filename.split("."))[0])
    imgnameList = []
    # 线程调用函数
    def tupshib(i):
        pool_sema.acquire()
        # print(i)
        yolo_result, type, num, t, imgname = detect2(i)
        imgnameList.append(imgname)
        type = res_process.tranChin(type)
        resStr = (str(imgname).split("."))[0] + ":" + str(type)  # 结果拼接 格式如下 文件名：识别结果
        # print(resStr)
        imageData.append(resStr)

        # f.write(resStr + "\n")
        pool_sema.release()

    file_dir = "Y:/yolov5-base_2/yolov5-base/mul_images/" + fileListName + "/" + (f1.filename.split("."))[0] + "/"
    list_directory = os.listdir(folder_abs + (str(f1.filename).split("."))[0])  # 获取路径下所有文件 文件名 list类型

    ziList = []
    for i in list_directory:  # 将文件名传入ziList列表中
        ziList.append(i)
        # print(i)

    # for i in list_directory:
    #     print(i)

    theard_list = []  # 线程信息列表
    theardsl = len(list_directory)  # 文件总数
    for i in range(int(theardsl)):  # 创建线程信息 拼接图片路径 将线程信息及路径传入线程信息列表中
        t = Thread(target=tupshib, args=[str(file_dir) + str(ziList[i])])
        theard_list.append(t)
        # print(t)
    # return "hh"
    #  检查线程信息数量
    # print(len(theard_list))
    # for i in range(len(theard_list)):
    #     print(theard_list[i])
    # return "hehe theard"

    for t in theard_list:  # 遍历线程信息 逐个开启线程
        t.start()
    # for i in range(int(theardsl)):
    #     tupshib(str(file_dir) + str(ziList[i]))

    for t in theard_list:  # 等待线程结束
        t.join()

    # 创建结果文件
    fileName = fileListName + ".txt"
    f = open("Y:/yolov5-base_2/yolov5-base/mul_images/" + fileListName + "/" + fileName, 'a+', encoding=" utf-8")

    # 对结果列表进行处理并写入结果文件中
    # print(fileName)
    # print(imageData)

    for res in imageData:
        f.write(res + "\n")

    print(imgnameList)
    # 对结果文件进行打包回传


    return "hehe!"

from loguru import logger
@app.route('/write_xml', methods=['POST', 'GET'])
def write_xml():

    fileListName = str(uuid.uuid4())
    os.makedirs(r'Y:/yolov5-base_2/yolov5-base/mul_images/{}'.format(fileListName))  # 用户创建文件夹



    # 获取当前文件所在路径
    basepath = os.path.dirname(__file__)
    basepath = basepath.replace("\\", '/')
    print((basepath))
    # 接收压缩包
    f1 = request.files['file']  # 发送参数的名字 要和这边接收参数的名字
    # 找到存储文件的位置
    upload_path = (basepath + '/mul_images/{}/'.format(fileListName) + f1.filename)  # 注意：没有的文件夹⼀定要先创建，不然会提示没有该路径
    print(upload_path)
    # 在位置中存储文件
    f1.save(upload_path)

    zip_file = zipfile.ZipFile(upload_path)  # 传入压缩文件 fileListName.zip 获取相关信息 并压缩
    zip_list = zip_file.namelist()
    # print(zip_list)
    folder_abs = "Y:/yolov5-base_2/yolov5-base/mul_images/{}/".format(fileListName)
    for f in zip_list:
        zip_file.extract(f, folder_abs)  # 循环解压文件到指定目录


    list_directory = os.listdir(basepath + '/mul_images/{}/'.format(fileListName) + str(f1.filename)[0:str(f1.filename).rfind(".")])
    # print(str(f1.filename)[0:str(f1.filename).rfind(".")])
    print(basepath + '/mul_images/{}/'.format(fileListName)  + str(f1.filename)[0:str(f1.filename).rfind(".")] +  '/' )
    filelists = []
    for directory in list_directory:
        filelists.append(directory)
    print("fileName", len(filelists))

    for i in range(len(filelists)):
        print(filelists[i])

    #创建xml文件夹
    os.makedirs(r'Y:/yolov5-base_2/yolov5-base/mul_images/{}'.format(fileListName) + '/' + "wrte_xml")  # 用户创建文件夹

    for i in range(len(filelists)):
        image_path = folder_abs + str(f1.filename)[0:str(f1.filename).rfind(".")] + '/' + filelists[i]
        print(image_path)
        xml_list, imageName = detect3(image_path)
        print("xml_list:", xml_list)
        print("imageName,filelists:", imageName, filelists[i])
        print("---------------------------------------------------")
        fileName = imageName + ".txt"

        f = open(folder_abs + '/' + "wrte_xml" + '/' + fileName, 'a+')
        f.write('''<annotation>
    <folder></folder>
    <filename></filename>
    <path></path>
    <source>
    <database>Unknown</database>
    </source>
    <size>
        <width></width>
        <height></height>
        <depth></depth>
    </size>
        <segmented></segmented>
            ''')

        for j in range(len(xml_list)):
            if len(xml_list[j]) <= 4 :
                break
            f.write('''
        <object>
            <name>{}</name>
            <pose>Unspecified</pose>
            <truncated>1</truncated>
            <difficult>0</difficult>
            <bndbox>
                <xmin>{}</xmin>
                <ymin>{}</ymin>
                <xmax>{}</xmax>
                <ymax>{}</ymax>
            </bndbox>
        </object>

                '''.format(xml_list[j][4], xml_list[j][0], xml_list[j][1], xml_list[j][2], xml_list[j][3]))


        f.write("</annotation> ")

    f.close()

    #打包
    def getZipDir(dirpath, outFullName):
        """
        压缩指定文件夹
        :param dirpath: 目标文件夹路径
        :param outFullName: 压缩文件保存路径+xxxx.zip
        :return: 无
        """
        zip = zipfile.ZipFile(outFullName, "w", zipfile.ZIP_DEFLATED)
        for path, dirnames, filenames in os.walk(dirpath):
            # 去掉目标跟路径，只对目标文件夹下边的文件及文件夹进行压缩
            fpath = path.replace(dirpath, '')

            for filename in filenames:
                zip.write(os.path.join(path, filename), os.path.join(fpath, filename))
        zip.close()
        logger.info("文件夹\"{0}\"已压缩为\"{1}\".".format(dirpath, outFullName))


    getZipDir(dirpath="Y:/yolov5-base_2/yolov5-base/mul_images/{}".format(fileListName) + '/' + "wrte_xml" ,
              outFullName="Y:/yolov5-base_2/yolov5-base/mul_images/{}".format(fileListName) + "/" + "write_xml.zip")



    return ""


# 这里面是顾哥解决跨域问题 产生的代码
from flask_cors import CORS, cross_origin

CORS(app, supports_credentials=True)  # 允许全局跨域

import argparse

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--weights', nargs='+', type=str,
                        default='runs/train/exp4_0629-allButNotStrength/weights/best.pt',
                        help='model.pt path(s)')
    parser.add_argument('--device', default='cpu', help='cuda device, i.e. 0 or 0,1,2,3 or cpu')
    parser.add_argument('--output', type=str, default='result', help='output folder')  # output folder
    parser.add_argument('--img-size', type=int, default=320, help='inference size (pixels)')
    parser.add_argument('--conf-thres', type=float, default=0.25, help='object confidence threshold')
    parser.add_argument('--iou-thres', type=float, default=0.45, help='IOU threshold for NMS')
    # parser.add_argument('--device', default='', help='cuda device, i.e. 0 or 0,1,2,3 or cpu')
    parser.add_argument('--view-img', action='store_true', help='display results')
    parser.add_argument('--save-txt', action='store_true', help='save results to *.txt')
    parser.add_argument('--classes', nargs='+', type=int, help='filter by class: --class 0, or --class 0 2 3')
    parser.add_argument('--agnostic-nms', action='store_true', help='class-agnostic NMS')
    parser.add_argument('--augment', action='store_true', help='augmented inference')
    parser.add_argument('--update', action='store_true', help='update all models')
    opt = parser.parse_args()

    app.run(host='0.0.0.0', port="5000", threaded=True)  # 这里如果绑定了 127.0.0.1 对方没办法请求
