import psycopg2 as psy
import numpy as np
import tensorflow as tf
from tensorflow import keras
from tensorflow.keras.preprocessing.text import Tokenizer
from tensorflow.keras.preprocessing.sequence import pad_sequences
from preprocess import CommentPreprocessor as cpr
from getData import RetrieveData as rd

conn = psy.connect(
    host = "ec2-34-194-158-176.compute-1.amazonaws.com",
    database = "da2g0o7m136sp5",
    user = "fzbeyehwmqhuxn",
    password = "7b04e1735374fcb6ba8f984fdcbcaaf5bada71f4d85df12c0e62cab2ca2b4022"
)
cur = conn.cursor()

data = rd(conn,cur)
bot_list = data.retrive_bot_names()

seq_dict = {}
labels = {}

for bot in bot_list:
    preprocess = cpr(bot,conn,cur)
    seq = preprocess.tokenize()
    seq_dict[bot] = seq
    data = rd(conn,cur)
    score = data.retrieve_bot_score(bot)
    if score > .5:
        labels[bot] = "is a bot"
    else:
        labels[bot] = "not a bot"
    

first_run = list(seq_dict.keys())[0]
labels_train = np.array(labels)

model = keras.Sequential([
    keras.layers.Embedding(seq_dict[first_run][0][0],16,input_length=100),
    keras.layers.GlobalAveragePooling1D(),
    keras.layers.Dense(24,activation="relu"),
    keras.layers.Dense(1, activation='sigmoid')
])


























