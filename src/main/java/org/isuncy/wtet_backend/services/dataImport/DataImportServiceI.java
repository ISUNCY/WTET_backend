package org.isuncy.wtet_backend.services.dataImport;

import org.isuncy.wtet_backend.entities.statics.Result;

public interface DataImportServiceI {
    Result<String> importDefaultData(String userId);
}
