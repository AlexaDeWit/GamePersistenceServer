Sequel.migration do
  up do
    create_table(:characters) do
      primary_key :id, :null=>false
      foreign_key :user_id, :null=>false
      String    :name, :null=>false, :unique=>true
    end
  end
  down do
    drop_table(:characters)
  end
end
