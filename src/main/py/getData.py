import psycopg2 as psy

class RetrieveData:
    def __init__(self,connection,cursor):
        self.conn = connection
        self.cur = cursor

    def retrive_bot_names(self):
        self.cur.execute("SELECT bot_name FROM comments")
        bot_list = []
        for row in self.cur:
            if row[0] not in bot_list:
                bot_list.append(row[0])
        return bot_list
    
    def retrieve_bot_score(self,bot):
        self.cur.execute("SELECT bot_score FROM comments WHERE bot_name = %s",(bot,))
        return self.cur.fetchone()
