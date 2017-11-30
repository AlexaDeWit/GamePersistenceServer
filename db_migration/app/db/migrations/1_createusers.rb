Sequel.migration do
  up do
    create_table(:USERS) do
      primary_key :ID, :null => false, type: :Bignum
      String :EMAIL, :null=>false, :unique=>true
      String :PASSWORD, :null=>false
      String :USERNAME, :null=>false, :unique=>true
      Boolean :VALIDATED_EMAIL, :null=>false
    end
    alter_table(:USERS) do
      set_column_default :VALIDATED_EMAIL, false
    end
  end
  down do
    drop_table(:USERS)
  end
end
