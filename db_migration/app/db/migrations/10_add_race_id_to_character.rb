Sequel.migration do
  up do
    alter_table(:CHARACTERS) do
      add_column :SPECIES_NAME, String, :null => false, :default => "Human"
    end
  end
  down do
    alter_table(:CHARACTERS) do
      drop_column :SPECIES_NAME
    end
  end
end
