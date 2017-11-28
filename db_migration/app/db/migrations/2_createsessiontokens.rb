Sequel.migration do
  up do
    create_table(:session_tokens) do
      primary_key :ID
      foreign_key :FK_USER_ID, :users
      String :TOKEN, :null=>false, :unique=>true
      DateTime :EXPIRATION, :null=>false
    end
  end
  down do
    drop_table(:session_tokens)
  end
end
