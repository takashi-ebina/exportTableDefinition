package com.export_table_definition.presentation.controller;

import com.export_table_definition.application.service.ExportTableDefinitionService;
import com.export_table_definition.application.service.impl.ExportTableDefinitionServiceImpl;
import com.export_table_definition.domain.writer.TableDefinitionWriter;

public class ExportTableDefinitionController {
	
	private final ExportTableDefinitionService service;
	
	public ExportTableDefinitionController() {
		service = new ExportTableDefinitionServiceImpl(new TableDefinitionWriter());
	}
	
	public void execute() {
		service.exportTableDefinition();
	};

}
