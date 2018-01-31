Sequel.migration do
  up do
    alter_table(:CHARACTERS) do
      set_column_type :LOCATION_X, 'float4'
      set_column_type :LOCATION_Y, 'float4'
      set_column_type :LOCATION_Z, 'float4'
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
