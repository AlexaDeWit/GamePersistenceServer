Sequel.migration do
  up do
    alter_table(:characters) do
      add_column :location_x, Integer
      add_column :location_y, Integer
      add_column :location_z, Integer
      add_column :current_health, Float
      add_column :current_stamina, Float
      add_column :current_focus, Float
    end
  end
  down do
    alter_table(:characters) do
      drop_column :location_x, Integer
      drop_column :location_y, Integer
      drop_column :location_z, Integer
      drop_column :current_health, Float
      drop_column :current_stamina, Float
      drop_column :current_focus, Float
    end
  end
end
