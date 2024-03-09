from flask import Flask, jsonify, request

app = Flask(__name__)

productos = [
    {"id": 1, "nombre": "Camiseta", "precio": 20, "descripcion": "Una camiseta comoda y elegante."},
    {"id": 2, "nombre": "Pantalon", "precio": 30, "descripcion": "Un pantalon resistente y moderno."}
]

# Obtener información de todos los productos
@app.route('/productos', methods=['GET'])
def obtener_todos_los_productos():
    productos_completos = [{"id": producto["id"], "nombre": producto["nombre"], "precio": producto["precio"], "descripcion": producto["descripcion"]} for producto in productos]
    return jsonify(productos_completos)

# Obtener información de un producto por su nombre
@app.route('/producto', methods=['GET'])
def obtener_informacion_producto():
    nombre_producto = request.args.get('nombre')
    for producto in productos:
        if producto['nombre'] == nombre_producto:
            return jsonify(producto)
    return jsonify({"mensaje": "Producto no encontrado"}), 404

# Agregar un nuevo producto
@app.route('/producto', methods=['POST'])
def agregar_producto():
    data = request.json
    nuevo_producto = {
        "id": len(productos) + 1,
        "nombre": data['nombre'],
        "precio": data['precio'],
        "descripcion": data['descripcion']
    }
    productos.append(nuevo_producto)
    return jsonify({"mensaje": "Producto agregado correctamente", "producto": nuevo_producto}), 201

# Actualizar información de un producto
@app.route('/producto/<int:id>', methods=['PUT'])
def actualizar_producto(id):
    data = request.json
    for producto in productos:
        if producto['id'] == id:
            producto['nombre'] = data.get('nombre', producto['nombre'])
            producto['precio'] = data.get('precio', producto['precio'])
            producto['descripcion'] = data.get('descripcion', producto['descripcion'])
            return jsonify({"mensaje": "Producto actualizado correctamente", "producto": producto})
    return jsonify({"mensaje": "Producto no encontrado"}), 404

# Eliminar un producto
@app.route('/producto/<int:id>', methods=['DELETE'])
def eliminar_producto(id):
    for producto in productos:
        if producto['id'] == id:
            productos.remove(producto)
            return jsonify({"mensaje": "Producto eliminado correctamente"})
    return jsonify({"mensaje": "Producto no encontrado"}), 404

if __name__ == '__main__':
    app.run(debug=True, port=5004)
