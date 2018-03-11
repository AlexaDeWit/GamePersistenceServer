Sequel.migration do
  up do
    create_table(:CLIENTS) do
      primary_key :ID, :null=>false, type: 'varchar(32)'
      String :CLIENT_ID, :null=>false, :unique=>true
      String :CLIENT_SECRET, :null=>false, :unique=>true
    end
  end
  down do
    drop_table(:CLIENTS)
  end
end
