from pathlib import Path
import sqlite3
from datetime import datetime
import shutil
from flask import current_app, g
import string, random

db_directory = "./backend/"# usually launched from the project directory
db_path = db_directory+"database.db"
db_backups_path = db_directory+"backups/"

def get_db():
    if 'db' not in g:
        g.db = sqlite3.connect(
            db_path,
            detect_types=sqlite3.PARSE_DECLTYPES
        )
        g.db.row_factory = sqlite3.Row

    return g.db


def close_db(e=None):
    db = g.pop('db', None)

    if db is not None:
        db.commit()
        db.close()


# returns false ifalready exits
def _addToken(token: str, name):
    if token == "": return
    token = token.strip()
    cur = get_db().cursor()
    cur.execute("""SELECT username
                   FROM TOKENS
                   WHERE token=?
                """,
                ([token]))
    res = cur.fetchone()
    if ( res and res != name ): # if we have a resultl
        cur.close()
        return False
    
    else: #insert
        cur.execute("INSERT INTO TOKENS VALUES (?, ?)", (token, name))

        cur.close()
        return True

# endpoint functions  
def validateUser(name, password_hash):
    cur = get_db().cursor()
    cur.execute("""SELECT password
                   FROM ACCOUNTS
                   WHERE username=?
                """,
                ([name]))

    res = cur.fetchone()[0] #fetchone returns a row element with only the password

    if (not res): return False

    print(" RESULTAT: ", res)
    print(" ENVOYE: ", password_hash)
    close_db()
    return res == password_hash

def addUser(name, password_hash):
    cur = get_db().cursor()
    cur.execute("""SELECT username
                   FROM ACCOUNTS
                   WHERE username=?
                """,
                ([name]))
    if ( cur.fetchone() ): # if we have a resultl
        close_db()
        return False
    
    else: #insert
        cur.execute("INSERT INTO ACCOUNTS VALUES (?, ?)", (name, password_hash))
        close_db()
        return True

def createNewToken(username):
    tokenLength = 32
    token = ''.join(random.SystemRandom().choice(string.ascii_letters + string.digits) for _ in range(tokenLength))

    while (not _addToken(token, username)):

        token = ''.join(random.SystemRandom().choice(string.ascii_letters + string.digits) for _ in range(tokenLength))

    close_db()
    return token

def validateToken(token: str):
    if token == "": return "Empty token", 510
    token = token.strip()
    
    print("lengths? ", len(token))
    test = "rXTpSmjuVApUwlX67Mj5eCIkSGOrAiOo"
    for i in range(len(token)):
        print("char ", i, " : ", test[i], " ",  token[i])

    cur = get_db().cursor()
    cur.execute("""SELECT username
                   FROM TOKENS
                   WHERE token=?
                """,
                ([token]))
    res = cur.fetchone()
    if (res): # if we have a resultl
        cur.close()
        return res[0], 200
    
    else:
        return "", 510

rebuildDatabase = False
if __name__ == "__main__":
    # setup the DB https://www.geeksforgeeks.org/python-sqlite-create-table/

    if (rebuildDatabase):
        current_time = datetime.now()
        backup_path = db_backups_path + "database_backup_" + current_time.strftime("%d_%m_%Y_%Hh_%Mm_%Ss")
        
        try: 
            shutil.copy2(db_path,backup_path)
        except FileNotFoundError:
            Path(db_backups_path).mkdir(parents=True, exist_ok=True)
            # don't copy if doesnt exist.
            if (Path(db_path + "database").exists()):
                shutil.copy2(db_path,backup_path)

        connection_obj = sqlite3.connect(db_path)
        cursor_obj = connection_obj.cursor()


        cursor_obj.execute("DROP TABLE IF EXISTS ACCOUNTS")
        cursor_obj.execute("DROP TABLE IF EXISTS TOKENS")
        cursor_obj.execute("DROP TABLE IF EXISTS IMAGES")


        tableAccounts = """ CREATE TABLE ACCOUNTS (
                username VARCHAR(25) PRIMARY KEY NOT NULL,
                password VARCHAR(64) NOT NULL
            ); """ # passwords hashes should be 32 characters but just in case
        tableTokens = """ CREATE TABLE TOKENS (
            token CHAR(32) PRIMARY KEY NOT NULL,
            username VARCHAR(25) NOT NULL
        ); """

        # image paths. Date in YYYY-MM-DD-hh-mm-ss
        tableImages = """ CREATE TABLE IMAGES (
            username VARCHAR(25) NOT NULL,
            imageName VARCHAR(255) NOT NULL,
            imageCreationDate CHAR(20) NOT NULL,

            imagePath VARCHAR(255) NOT NULL,

            PRIMARY KEY (username, imageName, imageCreationDate)
        ); """

        cursor_obj.execute(tableAccounts)
        cursor_obj.execute(tableTokens)
        cursor_obj.execute(tableImages)
        print("Created Database at " + db_path)
        cursor_obj.close()
        connection_obj.close()

    connection_obj = sqlite3.connect(db_path)
    cursor_obj = connection_obj.cursor()

    #cursor_obj.execute("INSERT INTO ACCOUNTS VALUES ('Tomtom', 'testest')")

    #connection_obj.commit()

    res = cursor_obj.execute("SELECT username FROM ACCOUNTS")

    print("res: ", res.fetchall())

    cursor_obj.close()
    connection_obj.close()