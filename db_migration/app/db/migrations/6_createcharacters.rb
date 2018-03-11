Sequel.migration do
  up do
    create_table(:CHARACTERS) do
      primary_key :ID, :null=>false, type: 'varchar(32)'
      foreign_key :FK_USER_ID, :USERS, :null=>false, type: 'varchar(32)'
      String      :NAME, :null=>false, :unique=>true
      String      :SPECIES_NAME, :null => false, :default => "Human"
    end
  end
  down do
    drop_table(:CHARACTERS)
  end
end
