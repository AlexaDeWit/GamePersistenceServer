Sequel.migration do
  up do
    create_table(:users) do
      primary_key :id
      String :email, :null=>false, :unique=>true
      String :password, :null=>false
      String :username, :null=>false, :unique=>true
      Boolean :validated_email, :null=>false
    end
    alter_table(:users) do
      set_column_default :validated_email, false
    end
  end
  down do
    drop_table(:users)
  end
end
