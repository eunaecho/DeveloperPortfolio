import { useEffect, useState } from "react";
import { SafeAreaView, StyleSheet, Image, View, Text, TouchableOpacity} from "react-native";

export default function DetailPage( {route} ) {
    const [tip, setTip] = useState(route.params.detailTip);

    return(
        <>
            <SafeAreaView style={styles.container}>
                <Image source={{ uri:tip.image}} style={styles.imageContainer} ></Image>
                <View style={styles.descContainer}>
                    <Text style={styles.titleText}>{tip.title}</Text>
                    <Text style={styles.descText}>{tip.desc}</Text>

                    <TouchableOpacity style={styles.likeButton}>
                        <Text style={{textAlign: 'center'}}>팁 찜하기</Text>
                    </TouchableOpacity>
                </View>
            </SafeAreaView>
        </>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff'
    },
    imageContainer: {
        flex: 3,
        padding:10,
        width: '100%',
        height: '100%',
        borderRadius: 20,
    },
    descContainer: {
        flex:2,
        padding: 10
    },
    titleText: {
        fontSize: 20,
        fontWeight: 600,
        textAlign: 'center'
    }, 
    descText: {
        marginTop: 5,
        fontSize: 15,
        fontWeight: 200,
        lineHeight: 22
    },
    likeButton: {
        marginTop: 5,
        borderRadius: 15,
        backgroundColor: '#4ff234',
        width: 100,
        height: 50,
        justifyContent: 'center',
        alignSelf: 'center'
    }
})