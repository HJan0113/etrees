# import base64
# import datetime
#
# import pymysql
# import requests
#
#
#
# class DBQuery:
#     def __init__(self):
#         try:
#             # 连接数据库
#             self.conn = pymysql.connect(host='rm-bp10926iel9ng69jlxo.mysql.rds.aliyuncs.com', user='yaoyao',
#                                    password='${MYSQL_PASSWORD}',
#                                    db='frame', port=3306, charset='utf8')
#             print('连接数据库成功！')
#             self.cursor = self.conn.cursor()
#         except Exception as e:
#             print(e)
#     def db_execute(self,type,num,disposetime,time,img_bin):
#         cursor=self.cursor
#         sql_str="insert into fruitresult (type,num,disposetime,time,img_bin) values( "+"'"+type+"'"+","+"'"+num+"'"+","+"'"+disposetime+"'"+","+"'"+time+"'"+","+"'"+img_bin+"'"+")"#"'"+img_bin+"'"
#         # noinspection PyBroadException
#         try:  # 查询操作
#             print(sql_str)
#             cursor.execute(sql_str)
#             self.conn.commit()
#             return cursor.fetchall()
#         except Exception:  # 异常处理
#             print("-------------数据库执行出错------------")
#             self.conn.rollback()
#         finally:  # 断开数据库
#             self.conn.close()
#
#
