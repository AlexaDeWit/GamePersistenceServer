Sequel.migration do
  up do
    create_table(:session_tokens) do
      primary_key :id
      foreign_key :user_id, :users
      String :token, :null=>false, :unique=>true
      DateTime :expiration, :null=>false
    end
  end
  down do
    drop_table(:session_tokens)
  end
end
