Sequel.migration do
  up do
    alter_table(:USERS) do
      add_index :ID
    end
    alter_table(:SESSION_TOKENS) do
      add_index :ID
    end
    alter_table(:CLIENTS) do
      add_index :ID
    end
    alter_table(:SHARDS) do
      add_index :ID
    end
    alter_table(:CHARACTERS) do
      add_index :ID
    end
  end
  down do
    alter_table(:USERS) do
      drop_index :ID
    end
    alter_table(:SESSION_TOKENS) do
      drop_index :ID
    end
    alter_table(:CLIENTS) do
      drop_index :ID
    end
    alter_table(:SHARDS) do
      drop_index :ID
    end
    alter_table(:CHARACTERS) do
      drop_index :ID
    end
  end
end