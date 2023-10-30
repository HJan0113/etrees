from flask import Flask, render_template, request
from werkzeug.utils import secure_filename
import os
import cv2
import torch
from facenet_pytorch import MTCNN,InceptionResnetV1
import sys

app = Flask(__name__) # 初始化，创建程序实例，__name__是系统变量，该变量指的是本py⽂件的⽂件名

# 根据⽂件路径和模型获取⼈脸特征向量

def load_known_faces(dstImgPath, mtcnn, resnet):
    aligned = []
    # 读取图片
    knownImg = cv2.imread(dstImgPath)
    # 使⽤mtcnn检测⼈脸，返回【⼈脸数组】
    face = mtcnn(knownImg)

    if face is not None:
        aligned.append(face[0])
    else:
        print(dstImgPath + " 中未发现人脸，程序中断")
        sys.exit(0)

    aligned = torch.stack(aligned).to(device)
    with torch.no_grad():
        # 使⽤resnet模型获取⼈脸对应的特征向量
        known_faces_emb = resnet(aligned).detach().cpu()
    print(dstImgPath + " 中人脸已就位")
    return known_faces_emb, knownImg


# 根据欧式距离判断是否为同⼀⼈

def match_faces(faces_emb, known_faces_emb, threshold):
    isExistDst = False

    distance = (known_faces_emb[0] - faces_emb[0]).norm().item()
    print("两张图的余弦距离为 " + str(distance))
    print("设定的阈值为 " + str(threshold))
    if (distance < threshold):
        isExistDst = True
    return isExistDst,distance

# 添加路由
@app.route('/', methods=['POST', 'GET'])
def face():
    if request.method == 'POST':
        # 获取用户上传的图片
        f1 = request.files['original_pic']
        f2 = request.files['contrast_pic']
        # 获取当前文件所在路径
        basepath = os.path.dirname(__file__)
        # 找到存储图片的位置
        upload_path1 = os.path.join(basepath, 'static\images', secure_filename(f1.filename))  # 注意：没有的文件夹⼀定要先创建，不然会提示没有该路径
        upload_path2 = os.path.join(basepath, 'static\images', secure_filename(f2.filename)) # secure_filename()作用是提取除中文之外的文件名
        # 在位置中存储用户上传的图片
        f1.save(upload_path1)
        f2.save(upload_path2)

        # 获取设备
        device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
        # mtcnn模型加载【设置⽹络参数，进⾏⼈脸检测】
        mtcnn = MTCNN(min_face_size=12, thresholds=[0.2, 0.2, 0.3], keep_all=True, device=device)
        # InceptionResnetV1模型加载【⽤于获取⼈脸特征向量】
        resnet = InceptionResnetV1(pretrained='vggface2').eval().to(device)
        MatchThreshold = 0.8  # ⼈脸特征向量匹配阈值设置

        # 注意图⽚路径
        known_faces_emb, _ = load_known_faces(upload_path1, mtcnn, resnet)  # 已知⼈物图
        faces_emb, img = load_known_faces(upload_path2, mtcnn, resnet)  # 待检测⼈物图
        isExistDst,distance = match_faces(faces_emb, known_faces_emb, MatchThreshold)  # 参数分别为 待检测人脸特征向量、已知人脸特征向量、设置的阈值

        # ⼈脸匹配
        if isExistDst:
            result = "匹配"
            # 返回⼈脸框，概率，5个⼈脸关键点
            boxes1, prob1, landmarks1 = mtcnn.detect(_, landmarks=True)
            boxes, prob, landmarks = mtcnn.detect(img, landmarks=True)
            # 读取照片
            img1 = cv2.imread(upload_path1)
            img2 = cv2.imread(upload_path2)
            # 在图片上画出人脸框
            img1 = cv2.rectangle(img1, (int(boxes1[0][0]), int(boxes1[0][1])), (int(boxes1[0][2]), int(boxes1[0][3])) ,(255, 0, 0), 5)
            img2 = cv2.rectangle(img2,(int(boxes[0][0]),int(boxes[0][1])) , (int(boxes[0][2]),int(boxes[0][3])) ,(255,0,0) ,5 )
            # 保存照片
            if cv2.imwrite(upload_path1, img1):
                print("保存成功")
            else:
                print("保存失败")
            if cv2.imwrite(upload_path2, img2):
                print("保存成功")
            else:
                print("保存失败")

            print("匹配")
        else:
            result="不匹配"
            print("不匹配")
        return render_template('face_result.html',result=result,distance=distance,MatchThreshold=MatchThreshold, filename=secure_filename(f1.filename), filename2=secure_filename(f2.filename),known_faces_emb=known_faces_emb,faces_emb=faces_emb)
    return render_template('face_start.html',result="待提交",diatance="待计算",MatchThreshold="")


if __name__ == '__main__':
    device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
    app.run(debug=True)
