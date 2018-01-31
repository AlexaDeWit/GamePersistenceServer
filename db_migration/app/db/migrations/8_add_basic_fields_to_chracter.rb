Sequel.migration do
  up do
    alter_table(:CHARACTERS) do
      add_column :LOCATION_X, Integer
      add_column :LOCATION_Y, Integer
      add_column :LOCATION_Z, Integer
      add_column :CURRENT_HEALTH, 'float4'
      add_column :CURRENT_TOUGHNESS, 'float4'
      add_column :CURRENT_CONSTITUTION, 'float4'
      add_column :CURRENT_STAMINA, 'float4'
      add_column :CURRENT_ENDURANCE, 'float4'
      add_column :CURRENT_QUICKNESS, 'float4'
      add_column :CURRENT_FOCUS, 'float4'
      add_column :CURRENT_CLARITY, 'float4'
      add_column :CURRENT_WILLPOWER, 'float4'
    end
  end
  down do
    alter_table(:CHARACTERS) do
      drop_column :LOCATION_X
      drop_column :LOCATION_Y
      drop_column :LOCATION_Z
      drop_column :CURRENT_HEALTH
      drop_column :CURRENT_CONSTITUTION
      drop_column :CURRENT_TOUGHNESS
      drop_column :CURRENT_STAMINA
      drop_column :CURRENT_ENDURANCE
      drop_column :CURRENT_QUICKNESS
      drop_column :CURRENT_FOCUS
      drop_column :CURRENT_CLARITY
      drop_column :CURRENT_WILLPOWER
    end
  end
end
