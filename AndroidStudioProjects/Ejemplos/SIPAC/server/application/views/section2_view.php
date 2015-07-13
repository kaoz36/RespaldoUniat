<script src="<?=base_url();?>assets/script/itemcontrol.js"> </script>
<script src="<?=base_url();?>assets/script/section2script.js"> </script>
<section> 
	<section><p>Formulario de Objetos</p>
		<p>Tipo: <select name="tipo" id="tipoInput">
					<?php foreach ($tipos as $tipo):?>
								<option value="<?php echo $tipo->id_tipo;?>"><?php echo $tipo->nombre_tipo;?></option>
					<?php endforeach;?>
				</select></p>
		<p>Categoria: <select name="categoria" id="categoriaInput">
							<?php foreach ($categorias as $categoria):?>
								<option value="<?php echo $categoria->id_categoria;?>"><?php echo $categoria->nombre_categoria;?></option>
							<?php endforeach;?>
					  </select></p>
		<p>Nombre: <input type="text" name="nombre"  id="nombreInput" /></p>
		<p>Precio: <input type="text" name="precio"  id="precioInput" /></p>
		<p>Descripcion: <textarea name="descripcion" cols="45" rows="5" id="descripcionInput"/> </p>
		<p>Asset: <input type="text" name="asset"  id="assetInput" /></p>
		<button type="button" id="saveItemButton">Guardar</button>
	</section>
</section>