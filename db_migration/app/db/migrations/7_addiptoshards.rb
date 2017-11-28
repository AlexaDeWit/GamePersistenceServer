Sequel.migration do
  up do
    alter_table(:SHARDS) do
      add_column :CURRENT_IP, String
    end
  end
  down do
    alter_table(:SHARDS) do
      drop_column :CURRENT_IP
    end
  end
end
