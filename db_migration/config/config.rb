require 'sequel'
require 'logger'

SBDB = Sequel.connect('postgres://postgres:pgdev@192.168.1.87:5432/catsandbox',
  :max_connections => 10)
PGDB = Sequel.connect('postgres://postgres:pgdev@192.168.1.87:5432/postgres',
  :max_connections => 10)
