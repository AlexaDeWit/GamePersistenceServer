Sequel.migration do
  up do
    rename_column :users, :PASSWORD, :PASSWORD_DIGEST
  end
  down do
    rename_column :users, :PASSWORD_DIGEST, :PASSWORD
  end
end
