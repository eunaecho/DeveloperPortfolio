import { SafeAreaView, StyleSheet, Text, View, Image, Dimensions, TouchableOpacity } from "react-native"

const cardImage = 'https://storage.googleapis.com/sparta-image.appspot.com/lecture/about.png';

export default function AboutPage(){
    return(
        <SafeAreaView style={styles.container}>  
            <View style={styles.headerContainer}>
                <Text style={styles.titleText}>앱 개발 클래스에 오신 것을 환영합니다!</Text>
            </View>
            <View style={styles.cardContainer}>
                <Image source={{uri:cardImage}} style={{width:200, height:215, alignSelf:'center', paddingVertical: 15,  borderRadius: 20}}></Image>
                <Text style={{textAlign:"center", fontSize: 20, fontWeight:700, marginHorizontal: 10, marginBottom: 10,}}>많은 내용을 간결하게 담아내려 노력했습니다!</Text>
                <Text style={{textAlign:"center", fontSize: 15, fontWeight:400}}>
                    꼭 완주해서 모두 내것으로 만들어가자!
                </Text>
                <TouchableOpacity style={styles.aboutButton}>
                    <Text style={{textAlign:'center'}}>About Me</Text>
                </TouchableOpacity>
            </View>
            
        </SafeAreaView>
    );
}

const styles = StyleSheet.create({
    container: {
        flex:1,
        flexDirection: 'column',
        alignItems: 'center',
        backgroundColor: '453e243',
    },
    headerContainer: {
        flex: 1,
        justifyContent: 'center',
        paddingHorizontal: 20
    },
    titleText: {
        textAlign: 'center',
        fontSize: 30,
        fontWeight: 700,
        color: '#fff'
    },
    cardContainer: {
        flex: 3,
        borderRadius: 20,
        backgroundColor: '#fff',
        marginHorizontal: 30,
        marginBottom: 40,
        paddingHorizontal: 20
    }, 
    aboutButton: {
        margin: 10,
        borderRadius: 20,
        alignSelf: 'center',
        width: 200,
        height: 70,
        backgroundColor: '#faf888',
        justifyContent: 'center'
    }
    
})