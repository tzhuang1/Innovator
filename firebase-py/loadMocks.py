import firebase_admin

databaseURL = 'https://innovator-solve.firebaseio.com/'

cred_obj = firebase_admin.credentials.Certificate('key.json')
default_app = firebase_admin.initialize_app(cred_obj, {
	'databaseURL':databaseURL
	})

from firebase_admin import db

ref = db.reference("/")

import json

with open("sample.json", "r") as f:
	file_contents = json.load(f)
ref.set(file_contents)