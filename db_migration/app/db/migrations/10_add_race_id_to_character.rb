Sequel.migration do
  up do
    alter_table(:characters) do
      add_column :species_id, String, :null => false, :default => "Human"
    end
  end
  down do
    alter_table(:characters) do
      drop_column :species_id
    end
  end
end
