Sequel.migration do
  up do
    alter_table(:CHARACTERS) do
      add_column :IS_NEW, TrueClass, :null => false, :default => true
    end
  end
  down do
    alter_table(:CHARACTERS) do
      drop_column :IS_NEW
    end
  end
end