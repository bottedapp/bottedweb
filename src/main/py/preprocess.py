import tensorflow as tf
from tensorflow import keras
from tensorflow.keras.preprocessing.text import Tokenizer
from tensorflow.keras.preprocessing.sequence import pad_sequences
from getData import RetrieveData
import psycopg2 as psy
import numpy as np
from psycopg2.extensions import register_adapter, AsIs

class CommentPreprocessor:
    def __init__(self,user,connection,cursor):
        self.conn = connection
        self.user = user
        self.cur = cursor
        self.sentences = self.get_sentences()
        self.word_index = {} #run tokenize to get index
    
    def get_sentences(self):
        sentences = []
        self.cur.execute("SELECT bot_comment FROM comments WHERE bot_name=%s",(self.user,))
        for row in self.cur:
            sentences.append(row[0])
        return sentences

    #tokenize and pad. return padded sequence
    def tokenize(self):
        tokenizer = Tokenizer(oov_token = "<OOV>")
        tokenizer.fit_on_texts(self.sentences)
        self.word_index = tokenizer.word_index
        sequences = tokenizer.texts_to_sequences(self.sentences)
        max_size = 0
        for sequence in sequences:
            if len(sequence) > max_size:
                max_size = len(sequence)
        padded = pad_sequences(
            sequences,padding='post',truncating='post',maxlen=max_size)
        return padded

class CommentPreprocessorUpload:
    def __init__(self,bot,connection,cursor,score):
        self.conn = connection
        self.cur = cursor
        self.user = bot
        self.score = score
        self.seq = CommentPreprocessor(self.user,self.conn,self.cur).tokenize()
        register_adapter(np.ndarray, self.addapt_numpy_array)

    def upload(self):
        query = """INSERT INTO prepro_data VALUES (%s,%s,%s);"""
        self.cur.execute(query,(self.user,self.score,self.seq[0]))
        self.conn.commit()

    def addapt_numpy_array(self,array):
        return AsIs(tuple(array))
    
    def what(self):
        print(type(self.seq))

   