Sequel.migration do
  up do
    create_table(:users) do
      primary_key :ID
      String :EMAIL, :null=>false, :unique=>true
      String :PASSWORD, :null=>false
      String :USERNAME, :null=>false, :unique=>true
      Boolean :VALIDATED_EMAIL, :null=>false
    end
    alter_table(:users) do
      set_column_default :VALIDATED_EMAIL, false
    end
  end
  down do
    drop_table(:users)
  end
end
