Sequel.migration do
  up do
    rename_column :users, :password, :password_digest
  end
  down do
    rename_column :users, :password_digest, :password
  end
end
