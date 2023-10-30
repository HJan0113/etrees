import json

import mysql_connect


# def process(type):
#     typeOnly = type.split("、")  # 不带回归参数的类别名 typeOnly is list
#     print(typeOnly)
#
#     mul_res_list = []  # 最终返回的json对象 含多个label的json对象
#     for i in range(len(typeOnly)):
#
#         # if typeOnly[i] == '':
#         #     continue
#
#         # print("type:",typeOnly)
#
#         res1, res2, res3 = mysql_connect.return_inf(typeOnly[i])  # 图鉴信息结果集
#         pro_list = mysql_connect.return_shop(typeOnly[i])  # 商品信息结果集
#
#         list_result = []  # list_result is a labels`s list
#
#         list_result.append({
#             'inf': {
#             'infIntroduce': res1,
#             'infOverview': res2,
#             'infTreatment': res3
#         }, 'shop':
#             pro_list
#         })
#
#         # results = json.dumps(json_result, ensure_ascii=False)  # results is a labels`s json
#         mul_res_list.append(list_result)
#
#     disInfo = []
#     disInfo.append({
#         "len": len(typeOnly),
#         "what": typeOnly
#     })
#     mul_res_list.append(disInfo)
#
#
#     mul_res_json = json.dumps(mul_res_list, ensure_ascii=False)
#     return mul_res_json

def process(type):
    typeOnly = type.split("、")  # 不带回归参数的类别名 typeOnly is list
    print(typeOnly)

    mul_res_list = []  # 最终返回的json对象 含多个label的json对象
    for i in range(len(typeOnly)):

        only_res = []

        inf = mysql_connect.return_inf(typeOnly[i])  # 图鉴信息结果集
        shop_list = mysql_connect.return_shop(typeOnly[i])  # 商品信息结果集

        only_res.append({
            "inf":inf,
            "shop":shop_list
        })
        mul_res_list.append(only_res)


    return mul_res_list


def tranChin(type):
    typeChin = ""
    typeOnly = type.split("、")  # 不带回归参数的类别名 typeOnly is list
    typeOnly = list(set(typeOnly))
    # print(typeOnly)
    for i in range(len(typeOnly)):
        if typeOnly[i] == '':
            continue
        typeChin = typeChin + what_disease(typeOnly[i]) + '、'
    # print(typeChin)
    typeChin = typeChin[0:len(typeChin)-1]
    return typeChin

def what_disease(typeOnly):
    if typeOnly + "" == "FQYBB":
        type = "番茄叶斑病"
    elif typeOnly + "" == "PGHXB":
        type = "苹果黑星病"
    elif typeOnly + "" == "PTHFB":
        type = "葡萄黑腐病"
    elif typeOnly + "" == "BCMBB":
        type = "白菜霉斑病"
    elif typeOnly + "" == "BCCD":
        type = "白菜虫洞"
    elif typeOnly + "" == "FQHYB":
        type = "番茄黄叶病"
    elif typeOnly + "" == "FQJQBFD":
        type = "番茄卷曲病毒"
    elif typeOnly + "" == "YMDYBB":
        type = "玉米大叶斑病"
    elif typeOnly + "" == "YMXYBB":
        type = "玉米小叶斑病"
    elif typeOnly + "" == "FQHYB":
        type = "番茄黄叶病"
    elif typeOnly + "" == "FQJQBD":
        type = "番茄卷曲病毒"
    elif typeOnly + "" == "CMBFB":
        type = "草莓白粉病"
    elif typeOnly + "" == "FQFMZMJ":
        type = "番茄粉末状霉菌"
    elif typeOnly + "" == "FQWYBSMJ":
        type = "番茄晚疫病水霉菌"
    elif typeOnly + "" == "PGXB":
        type = "苹果锈病"
    elif typeOnly + "" == "FQZYB":
        type = "番茄早疫病"
    elif typeOnly + "" == "PGHBB":
        type = "苹果灰斑病"

    return type



