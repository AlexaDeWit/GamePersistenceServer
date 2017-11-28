Sequel.migration do
  up do
    create_table(:shards) do
      primary_key :id, :null=>false
      foreign_key :client_id, :clients, :null=>false
      String    :name, :null=>false, :unique=>true
      String    :region
      Integer   :population
      Boolean   :accepting_connections
    end
  end
  down do
    drop_table(:shards)
  end
end
