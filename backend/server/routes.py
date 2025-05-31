
import db

# routes

def authenticate(username: str, password_hash: str):
    if db.validateUser(username, password_hash):

        userToken = db.createNewToken(username)

        return userToken, 200
    else:
        return "Wrong Password", 510
    
def validateToken(token: str):
    return db.validateToken(str(token)) # TODO maybe do a bit more in thisfunction?
    
def createAccount(username: str, password_hash: str):
    if db.addUser(username,password_hash):
        
        userToken = db.createNewToken(username)

        return userToken, 200
    else:
        return "User already exists", 510