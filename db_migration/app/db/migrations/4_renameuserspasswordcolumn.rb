Sequel.migration do
  up do
    rename_column :USERS, :PASSWORD, :PASSWORD_DIGEST
  end
  down do
    rename_column :USERS, :PASSWORD_DIGEST, :PASSWORD
  end
end
