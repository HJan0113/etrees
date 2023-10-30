# import pymysql
# import decimal
# import datetime
#
# conn = pymysql.connect(
#     host='47.96.66.129',  # 数据库地址
#     port=3306,  # 端口号
#     user='root',  # 用户名
#     password='${DB_PASSWORD}',  # 密码
#     db='etrees',  # 数据库名
#     charset='utf8mb4'  # 字符集
# )
#
# cursor = conn.cursor()
# print(cursor)
#
#
# # def mysql_cursor(): # 用于获取MySQL的连接
# #     conn = pymysql.connect(
# #         host='47.96.66.129',  # 数据库地址
# #         port=3306,  # 端口号
# #         user='root',  # 用户名
# #         password='${DB_PASSWORD}',  # 密码
# #         db='etrees',  # 数据库名
# #         charset='utf8mb4'  # 字符集
# #     )
# #
# #     cursor = conn.cursor()
# #     print(cursor)
#
# # def return_inf(res):
# #     # print(res)
# #     sql = "select * from data_info where concat(dir_name, name) =" + "\"" + res + "\""
# #
# #     # print(sql)
# #
# #     cursor.execute(sql)
# #     results = cursor.fetchall()
# #
# #     res1 = ""
# #     res2 = ""
# #     res3 = ""
# #     # print("inf",results)
# #
# #     for row in results:
# #         res1 = row[5]
# #         res2 = row[6]
# #         res3 = row[7]
# #
# #     return res1, res2, res3
# #
# #
# # def return_shop(res):
# #     # print(res)
# #     sql = "select * from shop_product where push_key =" + "\"" + res + "\""
# #     # print(sql)
# #     cursor.execute(sql)
# #     results = cursor.fetchall()
# #
# #     res1 = ""
# #     res2 = ""
# #     res3 = ""
# #     mul_pro_list = []
# #     # print("shop",results)
# #     for row in results:
# #         res1 = row[7]
# #
# #         price = row[5]
# #         price = str(decimal.Decimal(price).quantize(decimal.Decimal('0.00')))
# #         res2 = price
# #
# #         res3 = row[3]
# #
# #         mul_pro_list.append({
# #             'proImage': res1,
# #             'proPrice': res2,
# #             'proName': res3
# #         })
# #     return mul_pro_list
#
# def return_inf(res):  # 返回编号
#     sql = "select id from data_info where concat(dir_name, name) =" + "\"" + res + "\""
#     cursor.execute(sql)
#     results = cursor.fetchall()
#     return results[0]
#
#
# def return_shop(res):  # 返回编号
#     # print(res)
#     sql = "select id from shop_product where push_key =" + "\"" + res + "\""
#     # print(sql)
#     cursor.execute(sql)
#     results = cursor.fetchall()
#
#     mul_shop = []
#
#     for num in range(len(results)):
#         mul_shop.append((results[num])[0])
#
#     return mul_shop
#
#
# def write_mysql(user_id, device, image_url, res1):  # 写入后台 识别记录
#
#
#     deviceStr = ""
#     if device == "1":
#         deviceStr = "手机"
#     if device == "2":
#         deviceStr = "电脑"
#     time_now = datetime.datetime.now()
#
#     user_id = int(user_id)
#
#     sql = "insert into identify_record values (null, {}, '{}', '{}', '{}', '{}')".format(user_id, res1, deviceStr,
#                                                                                          time_now, image_url)
#
#     cursor.execute(sql)
#     # results = cursor.fetchall()
#     conn.commit()
#
#     return "write in mysql sucess!"
