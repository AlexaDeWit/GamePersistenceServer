Sequel.migration do
  up do
    alter_table(:shards) do
      add_column :current_ip, String
    end
  end
  down do
    alter_table(:shards) do
      drop_column :current_ip
    end
  end
end
