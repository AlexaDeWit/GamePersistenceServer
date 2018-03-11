Sequel.migration do
  up do
    drop_table(:SESSION_TOKENS)
  end
  down do
    create_table(:SESSION_TOKENS) do
      primary_key :ID, :null => false, type: 'varchar(64)'
      foreign_key :FK_USER_ID, :USERS, :null => false, type: 'varchar(64)'
      String :TOKEN, :null=>false, :unique=>true
      DateTime :EXPIRATION, :null=>false
    end
    alter_table(:SESSION_TOKENS) do
      add_index :ID
    end
  end
end