Sequel.migration do
  up do
    create_table(:SESSION_TOKENS) do
      primary_key :ID, :null => false, type: 'varchar(32)'
      foreign_key :FK_USER_ID, :USERS, :null => false, type: 'varchar(32)'
      String :TOKEN, :null=>false, :unique=>true
      DateTime :EXPIRATION, :null=>false
    end
  end
  down do
    drop_table(:SESSION_TOKENS)
  end
end
