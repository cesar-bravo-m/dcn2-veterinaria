package com.function.clientes;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import java.io.InputStreamReader;

public class ClienteGraphQL {

    private final DatabaseService databaseService = new DatabaseService();
    private GraphQL graphQL;

    public ClienteGraphQL() {
        init();
    }

    private void init() {
        // 1. Cargar el archivo schema.graphqls desde /resources
        InputStreamReader reader = new InputStreamReader(
                getClass().getResourceAsStream("/schema.graphqls")
        );
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(reader);

        // 2. Definir resolvers (queries y mutations)
        RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
                .type("Query", builder -> builder
                        .dataFetcher("clientes", env -> databaseService.getAllClientes())
                        .dataFetcher("clienteById", env -> {
                            Long id = Long.parseLong(env.getArgument("id"));
                            return databaseService.getClienteById(id);
                        })
                )
                .type("Mutation", builder -> builder
                        .dataFetcher("createCliente", env -> {
                            ClienteDTO cliente = new ClienteDTO();
                            cliente.setNombre(env.getArgument("nombre"));
                            cliente.setPaterno(env.getArgument("paterno"));
                            cliente.setMaterno(env.getArgument("materno"));
                            cliente.setRut(env.getArgument("rut"));
                            return databaseService.createCliente(cliente);
                        })
                        .dataFetcher("updateCliente", env -> {
                            ClienteDTO cliente = new ClienteDTO();
                            cliente.setClienteId(Long.parseLong(env.getArgument("clienteId")));
                            cliente.setNombre(env.getArgument("nombre"));
                            cliente.setPaterno(env.getArgument("paterno"));
                            cliente.setMaterno(env.getArgument("materno"));
                            cliente.setRut(env.getArgument("rut"));
                            return databaseService.updateCliente(cliente);
                        })
                        .dataFetcher("deleteCliente", env -> {
                            Long id = Long.parseLong(env.getArgument("clienteId"));
                            return databaseService.deleteCliente(id);
                        })
                )
                .build();

        // 3. Crear GraphQLSchema uniendo typeRegistry y wiring
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);

        // 4. Inicializar el motor GraphQL
        this.graphQL = GraphQL.newGraphQL(schema).build();
    }

    public GraphQL getGraphQL() {
        return graphQL;
    }
}
