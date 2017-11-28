Sequel.migration do
  up do
    alter_table(:characters) do
      set_column_type :location_x, Float
      set_column_type :location_y, Float
      set_column_type :location_z, Float
    end
  end
  down do
    alter_table(:characters) do
      set_column_type :location_x, Integer
      set_column_type :location_y, Integer
      set_column_type :location_z, Integer
    end
  end
end
