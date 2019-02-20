#!/usr/bin/python
from psycopg2 import connect
import sys
from psycopg2.extensions import ISOLATION_LEVEL_AUTOCOMMIT

con = None
dbname = 'financial_db'
con = connect(user='postgres', host='localhost', password='postgres', database=dbname)

con.set_isolation_level(ISOLATION_LEVEL_AUTOCOMMIT)
cur = con.cursor()
try:
    cur.execute('DROP DATABASE IF EXISTS ' + dbname)
except:
    print('Error')
try:
    cur.execute('CREATE DATABASE IF NOT EXISTS ' + dbname)
    print('Created ' + dbname)
except:
    print 'Database already created... skipping'

# DROP OLD TABLES
cur.execute('DROP TABLE IF EXISTS transactionalItem')
cur.execute('DROP TABLE IF EXISTS budgetItem')
cur.execute('DROP TABLE IF EXISTS account')
# Load table definitions
    
try:
    accountTable = open('tables/account.sql', 'r')
    cur.execute(accountTable.read())
    accountTable.close()
    print 'Created account table'
except:
    print 'Skipping account table...'

try:
    transactionalItem = open('tables/transactionItem.sql', 'r')
    cur.execute(transactionalItem.read())
    transactionalItem.close()
    print 'Created transactionalItem table'
except:
    print 'Skipping transactionalItem table...'

try:
    budgetItem = open('tables/budgetItem.sql', 'r')
    cur.execute(budgetItem.read())
    budgetItem.close()
    print 'Created budgetItem table'
except:
    print 'Skipping budgetItem table...'

try:
    accountTestData = open('data/accountTestData.sql', 'r')
    cur.execute(accountTestData.read())
    accountTestData.close()
    print 'Loaded account data'
except:
    print 'Failed to load account test data'

try:
    transactionalTestData = open('data/transactionalTestData.sql', 'r')
    cur.execute(transactionalTestData.read())
    transactionalTestData.close()
    print 'Loaded transactional data'
except:
    print 'Failed to load transactional test data'

try:
    budgetTestData = open('data/budgetTestData.sql', 'r')
    cur.execute(budgetTestData.read())
    transactionalTestData.close()
    print 'Loaded budget data'
except:
    print 'Failed to load budget test data'


cur.close()
con.close()
