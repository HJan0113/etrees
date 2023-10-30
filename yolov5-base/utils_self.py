def how_long(t1, t2, t3, t4, t5, t6):
    print("接收参数", t2 - t1)
    print("调用模型进行推理", t3 - t2)
    print("将图片上传至OSS", t4 - t3)
    print("结果处理", t5 - t4)
    print("将行为录入识别记录",t6 - t5)
    print("全程共耗时",t6 - t1)