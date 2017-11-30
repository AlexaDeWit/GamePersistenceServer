Sequel.migration do
  up do
    create_table(:SHARDS) do
      primary_key :ID, :null=>false, type: :Bignum
      foreign_key :FK_CLIENT_ID, :CLIENTS, :null=>false, type: :Bignum
      String    :NAME, :null=>false, :unique=>true
      String    :REGION, :default => "EU"
      Integer   :POPULATION, :default => 0
      Boolean   :ACCEPTING_CONNECTIONS, :null => false, :default => true
    end
  end
  down do
    drop_table(:SHARDS)
  end
end
