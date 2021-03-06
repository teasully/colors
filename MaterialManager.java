package project.etrumper.thomas.ghostbutton;

import android.util.Log;

import java.util.Vector;

/**
 * Created by thoma on 2/12/2016.
 */
public class MaterialManager{

    static Material[] materials;

    private static Material saveMaterial;

    static ColorPalette currentPalette, yankeePalette1;

    public static void init() {
        materials = new Material[0];
        // Basic COLORS
        newMaterial("LightBlue", new float[]{0.1f, 0.1f, 0.8f});
        newMaterial("Black", new float[]{0.2f, 0.2f, 0.2f});
        newMaterial("Grey", new float[]{0.47f, 0.47f, 0.47f});
        newMaterial("White", new float[]{1f, 1f, 1f});
        newMaterial("Pink", new float[]{0.8f, 0f, 0.6f});
        newMaterial("Red", new float[]{0.8f, 0.f, 0.f});
        newMaterial("Green", new float[]{0.f, 0.8f, 0.f});
        newMaterial("Blue", new float[]{0.f, 0.f, 0.8f});
        newMaterial("Brown", new float[]{.2f, .1f, 0.1f});
        // Palette one
        yankeePalette1 = new ColorPalette("Yankee Blue Palette", null);
        yankeePalette1.addColors(
                newMaterial("Yankee Blue", new float[]{31f / 255f, 32f / 255f, 65f / 255f}),
                newMaterial("Sandstorm", new float[]{238f / 255f, 198f / 255f, 67f / 255f}),
                newMaterial("Antiflash White", new float[]{238f / 255f, 240f / 255f, 242f / 255f}),
                newMaterial("Indigo Dye", new float[]{13f / 255f, 33f / 255f, 161f / 255f}),
                newMaterial("Pastel Green", new float[]{122f / 255f, 229f / 255f, 130f / 255f})
        );
        // Set current palette
        currentPalette = yankeePalette1;
        // Engine materials
        newMaterial("Letter", new float[]{.9f, 0.5f, 0.9f});
        // Game materials
        newMaterial("Cube", new float[]{0.6f, 0.1f, 0.f});
        newMaterial("Wall", new float[]{39f / 255f, 33f / 255f, 60f / 255f});
        newMaterial("Button", new float[]{0f, 0f, 0f}, 0.5f);
        newMaterial("Arrow", new float[]{0f, 0f, 0f}, 0.5f);
    }

    public static Vector3f getColorPalette(int index){
        return currentPalette.getColor(index);
    }

    public static void changeMaterialColor(String matName, Vector3f color) { // // TODO: 5/26/2016 change swordColor to global
        if(materials == null || materials.length == 0){
            init();
        }
        if (!isMaterial(matName)) {
            // Create new material if does not exist
            getMaterial(matName);
        }
        if (color == null) {
            return;
        }
        int index = getMaterialIndex(matName);
        String programName = materials[index].programName;
        float opacity = materials[index].opacity;

        materials[index] = new Material(matName, programName, new LightProperties(color.vector, color.vector, color.vector), GameConstants.defaultShininess, opacity);
    }

    public static boolean isMaterial(String materialName) {
        for (Material material : materials) {
            if (material.name.equals(materialName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean saveMaterial(String materialName){
        for(Material material : materials){
            if(material.name.equals(materialName)){
                saveMaterial = new Material(material.name, material.programName, material.lightProperties, material.shininess, material.opacity);
                return true;
            }
        }
        //Log.e("MaterialManager", "Trying to save null material " + materialName);
        return false;
    }

    public static boolean loadSavedMaterialOnto(String materialName){
        for(int i = 0; i < materials.length; i++){
            if(materials[i].name.equals(materialName)){
                materials[i] = saveMaterial;
                return true;
            }
        }
        return false;
    }

    public static Vector3f getVector3fColor(String mat){
        Material material = getMaterial(mat);
        if(material == null){
            Log.e("MaterialManager", "Material not found, returning null");
            return null;
        }
        return new Vector3f(material.lightProperties.diffuse);
    }

    public static boolean restoreSavedMaterial(){
        if(saveMaterial == null){
            //Log.e("MaterialManager", "Trying to restoreSavedMaterial on null savedMaterial");
            return false;
        }
        for(int i = 0; i < materials.length; i++){
            if(materials[i].name.equals(saveMaterial.name)){
                materials[i] = saveMaterial;
                return true;
            }
        }
        return false;
    }

    public static int getMaterialIndex(String materialName){
        int i = 0;
        for(Material material : materials){
            if(material.name.equals(materialName)){
                return i;
            }
            i++;
        }
        return -1;
    }

    public static Material getMaterial(String materialn){
        try {
            for (Material material : materials) {
                if (material.name.equals(materialn)) {
                    return material;
                }
            }
        }catch(Exception e){
            return newMaterial(materialn, new float[]{.9f, 0.5f, 0.9f});
        }
        Log.e("MaterialManager", "Could not find material " + materialn + ".. creating fake");
        return newMaterial(materialn, new float[]{.9f, 0.5f, 0.9f});
    }

    /*public static boolean saveMaterials(String filename, Context context){

        File file = context.getFileStreamPath(filename);

        if(!file.exists()){
            try {
                file.createNewFile();
            }catch(IOException e){
                Log.e("saveMaterials()", e.toString());
                return false;
            }
            FileOutputStream writer = null;
            try {
                writer = context.openFileOutput(file.getName(), Context.MODE_PRIVATE);
            }catch(FileNotFoundException e){
                Log.e("saveMaterial()", e.toString());
                return false;
            }
            for(Material material : materials){
                if(material != null){
                    String data = String.format("%s %f %f %f", material.name,
                            material.lightProperties.diffuse[0],
                            material.lightProperties.diffuse[1],
                            material.lightProperties.diffuse[2]);
                    try {
                        writer.write(data.getBytes());
                        writer.flush();
                    }catch (IOException e){
                        Log.e("dsaveMaterials()", e.toString());
                        return false;
                    }
                }
            }
            try {
                writer.close();
            }catch (IOException e){
                Log.e("saveMaterials", e.toString());
            }
        }

        return true;
    }*/

    public static Material newMaterial(String name, String programName, float[] color, float opacity){
        if(materials == null){
            init();
        }
        for(Material cmaterial : materials){
            if(cmaterial.name.equals(name)){
                return cmaterial;
            }
        }
        Material material = new Material(name, programName, new LightProperties(color, color, color), GameConstants.defaultShininess, opacity);
        Material[] temp = new Material[materials.length + 1];
        System.arraycopy(materials, 0, temp, 0, materials.length);
        temp[materials.length] = material;
        materials = temp;
        return material;
    }

    public static Material newMaterial(String name, float[] color, float opacity){
        return newMaterial(name, SuperManager.defaultProgramName, color, opacity);
    }

    public static Material newMaterial(String name, String programName, float[] color){
        return newMaterial(name, programName, color, 1f);
    }

    public static Material newMaterial(String name, float[] color){
        return newMaterial(name, color, 1f);
    }

}
