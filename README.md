


This is a maven project with a sample java application that aims to showcase the use of managed identities in Azure by accessing an Azure SQL Database from am Virtual Machine in Azure.

It is based in this turorial from Microsoft : [Tutorial: Use a Windows VM system-assigned managed identity to access Azure SQL](https://docs.microsoft.com/en-us/azure/active-directory/managed-identities-azure-resources/tutorial-windows-vm-access-sql)

This simple application accesses a sample Azure SQL Database. It should be executed from a VM with a managed identity. 

Steps:
- Deploy an Azure VM of your choice and enable system-managed identity (see [here](https://docs.microsoft.com/en-us/azure/active-directory/managed-identities-azure-resources/tutorial-windows-vm-access-sql#enable) )
- [Create an Azure SQL Database](https://docs.microsoft.com/en-us/azure/azure-sql/database/single-database-create-quickstart) and grant access to your VM as described [here](https://docs.microsoft.com/en-us/azure/active-directory/managed-identities-azure-resources/tutorial-windows-vm-access-sql#grant-access)
- Access the data by running the sample application from the VM:
e.g. by running:
```
mvn compile exec:java -Dexec.mainClass="mi.AzMiSample"
```

# allianz-sqlmi-demo
