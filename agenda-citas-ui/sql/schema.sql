Create Database if not exists;

Use agenda_citas_db;

Drop table if exists cita;

create table cita(
id int  Auto_increment primary Key,
cliente varchar(100) not null,
fecha_hora Datetime not null,
servicio Varchar (200) not null,
duracion_minutos int not null check(duracion_minutos> 0),
estado enum ('pendiente', 'confirmada','cancelada') not null default 'pendiente'
)ENGINE=InnoDB;

Insert into cita (cliente, fecha_hora, servicio, duracion_minutos, estado) values
('Maria Fernanda Lopez','2026-09-20 14:30:00','Corte de cabello',45,'pendiente'),
('Robert Mendez','2026-09-21 09:00:00','Cambio de aceite',60,'confirmada'),
('Ursula Juarez','2026-09-18 16:00:00','Consulta general',30,'cancelada');

