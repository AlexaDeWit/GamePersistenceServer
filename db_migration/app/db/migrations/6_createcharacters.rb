Sequel.migration do
  up do
    create_table(:CHARACTERS) do
      primary_key :ID, :null=>false
      foreign_key :FK_USER_ID, :null=>false
      String      :NAME, :null=>false, :unique=>true
    end
  end
  down do
    drop_table(:CHARACTERS)
  end
end
