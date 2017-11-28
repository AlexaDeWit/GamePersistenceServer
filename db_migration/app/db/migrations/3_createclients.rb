Sequel.migration do
  up do
    create_table(:clients) do
      primary_key :id, :null=>false
      String :client_id, :null=>false, :unique=>true
      String :client_secret, :null=>false, :unique=>true
    end
  end
  down do
    drop_table(:clients)
  end
end
