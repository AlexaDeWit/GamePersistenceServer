Sequel.migration do
  up do
    create_table(:SESSION_TOKENS) do
      primary_key :ID
      foreign_key :FK_USER_ID, :USERS
      String :TOKEN, :null=>false, :unique=>true
      DateTime :EXPIRATION, :null=>false
    end
  end
  down do
    drop_table(:SESSION_TOKENS)
  end
end
