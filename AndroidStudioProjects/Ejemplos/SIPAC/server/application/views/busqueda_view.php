<html>
<head>
	<title>Registro de Busquedas</title>
	<link rel="stylesheet" href="<?=base_url();?>assets/css/styles.css" type="text/css" media="screen" />
	<style>

	   table {
		    border: 1px solid;
		    padding: 0;
		    width: 80%;
		}	

		td {   
		    margin: 0;
		    padding: 0;
		    text-align: center;
		}


		th {
		    background: none repeat scroll 0 0 #404040;
		}
	</style>
</head>
<body>

<p>Busquedas</p>
<table cellspacing="0" border="1">
	<tr>
		<th>Id del Usuario</th><th>Posici&oacute;n</th><th>Fecha</th>
	</tr>
	<?php foreach ($data as $dataItem):?>
	<tr>
		<td><?php echo $dataItem->id_usuario;?></td>
		<td>(<?php echo $dataItem->latitude;?>&nbsp;,&nbsp;<?php echo $dataItem->longitude;?>)</td>
		<td><?php echo $dataItem->fecha;?></td>
	</tr>
	<?php endforeach;?>
</table>
</body>
</html>