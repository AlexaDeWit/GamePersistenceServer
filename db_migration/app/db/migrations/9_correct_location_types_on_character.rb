Sequel.migration do
  up do
    alter_table(:CHARACTERS) do
      set_column_type :LOCATION_X, Float
      set_column_type :LOCATION_Y, Float
      set_column_type :LOCATION_Z, Float
    end
  end
  down do
    alter_table(:CHARACTERS) do
      set_column_type :LOCATION_X, Integer
      set_column_type :LOCATION_Y, Integer
      set_column_type :LOCATION_Z, Integer
    end
  end
end
