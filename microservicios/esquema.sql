CREATE TABLE clientes (
    cliente_id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255),
    paterno VARCHAR(255),
    materno VARCHAR(255),
    rut VARCHAR(255)
);

CREATE TABLE mascotas (
    mascota_id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT,
    nombre VARCHAR(255),
    especie VARCHAR(255),
    edad INTEGER,
    domestico BOOLEAN
);

CREATE TABLE vt_persona (
    persona_id BIGSERIAL PRIMARY KEY,
    nombres VARCHAR(255),
    paterno VARCHAR(255),
    materno VARCHAR(255),
    rut VARCHAR(255),
    cargo_id BIGINT,
    pacienteFlag BOOLEAN,
    funcionario_flag BOOLEAN,
    registro_fecha TIMESTAMP
);

CREATE TABLE vt_inventario (
    inventario_id BIGSERIAL PRIMARY KEY,
    inventario_tipo_id BIGINT,
    nombre VARCHAR(255),
    medida_unidad_id BIGINT,
    stock BIGINT,
    usuario_id BIGINT
);

CREATE TABLE vt_historial (
    historial_id BIGSERIAL PRIMARY KEY,
    mascota_id BIGINT,
    inventario_id BIGINT,
    cantidad BIGINT,
    dosis_cantidad BIGINT,
    registro_fecha DATE
);

CREATE TABLE vt_agenda (
    agenda_id BIGSERIAL PRIMARY KEY,
    mascota_id BIGINT,
    fecha TIMESTAMP,
    nulo_flag BOOLEAN,
    nulo_fecha TIMESTAMP,
    registro_fecha TIMESTAMP,
    usuario_id BIGINT
);

CREATE TABLE laboratorio (
    laboratorio_id BIGSERIAL PRIMARY KEY,
    mascota_id BIGINT,
    fecha TIMESTAMP,
    examen VARCHAR(255),
    resultado VARCHAR(255),
    valor DECIMAL,
    unidad VARCHAR(255)
);

CREATE TABLE mensajes (
    mensaje_id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT,
    fecha_envio TIMESTAMP,
    contenido TEXT,
    estado VARCHAR(255)
);

CREATE TABLE documentos (
    documento_id BIGSERIAL PRIMARY KEY,
    documento_tipo_id BIGINT,
    impuesto_id BIGINT,
    documento_numero BIGINT,
    documento_fecha TIMESTAMP,
    registro_fecha TIMESTAMP,
    usuario_id BIGINT
);

ALTER TABLE mascotas ADD CONSTRAINT fk_mascotas_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(cliente_id);
ALTER TABLE vt_historial ADD CONSTRAINT fk_historial_mascota FOREIGN KEY (mascota_id) REFERENCES mascotas(mascota_id);
ALTER TABLE vt_historial ADD CONSTRAINT fk_historial_inventario FOREIGN KEY (inventario_id) REFERENCES vt_inventario(inventario_id);
ALTER TABLE vt_agenda ADD CONSTRAINT fk_agenda_mascota FOREIGN KEY (mascota_id) REFERENCES mascotas(mascota_id);
ALTER TABLE laboratorio ADD CONSTRAINT fk_laboratorio_mascota FOREIGN KEY (mascota_id) REFERENCES mascotas(mascota_id);
ALTER TABLE mensajes ADD CONSTRAINT fk_mensajes_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(cliente_id);