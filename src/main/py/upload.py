import psycopg2 as psy
from preprocess import CommentPreprocessor,CommentPreprocessorUpload
from getData import RetrieveData as rd


conn = conn = psy.connect(
    host = "ec2-34-194-158-176.compute-1.amazonaws.com",
    database = "da2g0o7m136sp5",
    user = "fzbeyehwmqhuxn",
    password = "7b04e1735374fcb6ba8f984fdcbcaaf5bada71f4d85df12c0e62cab2ca2b4022"
)
cur = conn.cursor()

bots = rd(conn,cur).retrive_bot_names()

for bot in bots:
    score = rd(conn,cur).retrieve_bot_score(bot)
    CommentPreprocessorUpload(bot,conn,cur,score).upload()

    