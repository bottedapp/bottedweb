import psycopg2 as psy
import numpy as np
import tensorflow as tf
from tensorflow import keras
from tensorflow.keras.preprocessing.text import Tokenizer
from tensorflow.keras.preprocessing.sequence import pad_sequences
from preprocess import CommentPreprocessor as cpr
from getData import RetrieveData as rd
import pickle


conn = psy.connect(
    host = "ec2-34-194-158-176.compute-1.amazonaws.com",
    database = "da2g0o7m136sp5",
    user = "fzbeyehwmqhuxn",
    password = "7b04e1735374fcb6ba8f984fdcbcaaf5bada71f4d85df12c0e62cab2ca2b4022"
)
cur = conn.cursor()

data = rd(conn,cur)
bot_list = data.retrive_bot_names()

sequences = []
labels = []

rows = data.retrieve_prepro_data()






























