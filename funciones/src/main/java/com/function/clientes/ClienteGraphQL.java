package com.function.clientes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import graphql.ExecutionInput;
import graphql.GraphQL;
import com.microsoft.azure.functions.ExecutionContext;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLObjectType;

@SuppressWarnings("deprecation")
public class ClienteGraphQL {

    private static GraphQL graphQL;

    static {

        var clienteType = GraphQLObjectType.newObject()
                .name("Cliente")
                .field(field -> field
                        .name("id")
                        .type(graphql.Scalars.GraphQLID))
                .field(field -> field
                        .name("nombre")
                        .type(graphql.Scalars.GraphQLString))
                .field(field -> field
                        .name("ubicacion")
                        .type(graphql.Scalars.GraphQLString))
                .build();

        var clientes = List.of(
            Map.of("id", "1", "nombre", "Cliente Central", "ubicacion", "Calle 123"),
            Map.of("id", "2", "nombre", "Cliente Norte", "ubicacion",  "Avenida 456"),
            Map.of("id", "3", "nombre", "Quinta", "ubicacion",  "Calle 123 321")
        );

        DataFetcher<List<Map<String, String>>> clientesDataFetcher = environment -> {
            String id = environment.getArgument("id");
            return clientes.stream()
                .filter(cliente -> cliente.get("id").equals(id))
                .collect(Collectors.toList());
        };

        var queryType = GraphQLObjectType.newObject()
            .name("Query")
            .field(field -> field
                    .name("clientes")
                    .type(new GraphQLList(clienteType))
                    .argument(arg -> arg
                            .name("id")
                            .type(graphql.Scalars.GraphQLID))
                    .dataFetcher(clientesDataFetcher))
            .build();

        var schema = graphql.schema.GraphQLSchema.newSchema()
            .query(queryType)
            .build();

        graphQL = GraphQL.newGraphQL(schema).build();

    }

    @FunctionName("graphqlClientes")
    public HttpResponseMessage run(
        @HttpTrigger(
            name = "req",
            methods = {HttpMethod.POST},
            authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<Optional<Map<String, Object>>> request,
        final ExecutionContext context) {

        context.getLogger().info("GraphQL request received.");

        // Obtener el body del request
        Map<String, Object> body = request.getBody().orElse(new HashMap<>());

        // GraphQL usualmente manda "query" en el body
        String query = (String) body.get("query");

        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                .query(query)
                .build();

        Map<String, Object> result = graphQL.execute(executionInput).toSpecification();

        return request.createResponseBuilder(HttpStatus.OK)
                .body(result)
                .build();
    }


}
